package com.mpolina.cats.service;

import com.mpolina.cats.getdtos.AdminDto;
import com.mpolina.cats.getdtos.UserDto;
import com.mpolina.cats.dtomapping.AdminDtoMapping;
import com.mpolina.cats.dtomapping.UserDtoMapping;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.entity.user.User;
import com.mpolina.cats.entity.user.UserRole;
import com.mpolina.cats.exception.CatsLabServiceException;
import com.mpolina.cats.repository.OwnerJpaRepository;
import com.mpolina.cats.repository.UserJpaRepository;
import com.mpolina.cats.requestmodels.user.AdminModel;
import com.mpolina.cats.requestmodels.user.GetUserModel;
import com.mpolina.cats.requestmodels.user.UpdateUserModel;
import com.mpolina.cats.requestmodels.user.UserModel;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final OwnerJpaRepository ownerRepository;
    private final UserJpaRepository userRepository;
    private final OwnerService ownerService;

    @Autowired
    public UserServiceImpl(OwnerJpaRepository ownerRepository,
                           UserJpaRepository userRepository,
                           OwnerService ownerService) {
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
        this.ownerService = ownerService;
    }

    public List<String> getUserRoles() {
        List<String> usersRoles = Stream.of(UserRole.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return usersRoles;
    }

    public AdminDto createAdmin(AdminModel adminModel) {
        if (adminModel == null){
            throw new CatsLabServiceException("adminModel is null while creating Admin.");
        }
        if (Strings.isEmpty(adminModel.username())) {
            throw new CatsLabServiceException("Username is incorrect while creating Admin.");
        }
        if (Strings.isEmpty(adminModel.password())) {
            throw new CatsLabServiceException("Password is incorrect while creating Admin.");
        }

        if (checkUsersEqualty(adminModel.password()) == true) {
            throw new CatsLabServiceException("User with name " + adminModel.username() + " already exists.");
        }

        User user = new User(adminModel.username(), passwordEncoder().encode(adminModel.password()), UserRole.ADMIN, null);

        userRepository.save(user);

        return AdminDtoMapping.asDto(user);
    }

    public UserDto createUser(UserModel userModel) {
        if (userModel == null){
            throw new CatsLabServiceException("adminModel is null while creating User.");
        }
        if (Strings.isEmpty(userModel.username())) {
            throw new CatsLabServiceException("Username is incorrect while creating User.");
        }
        if (Strings.isEmpty(userModel.password())) {
            throw new CatsLabServiceException("Password is incorrect while creating User.");
        }

        if (checkUsersEqualty(userModel.username()) == true) {
            throw new CatsLabServiceException("User with name " + userModel.username() + " already exists.");
        }
        if (userModel.ownerId() == null) {
            throw new CatsLabServiceException("Null ownerId while creating User.");
        }

        Optional<Owner> foundOwnerOptional = ownerRepository.findById(userModel.ownerId());
        if (!foundOwnerOptional.isPresent()) {
            throw new CatsLabServiceException("No owner with this Id while creating User.");
        }

        User user = new User(userModel.username(), passwordEncoder().encode(userModel.password()), UserRole.USER, foundOwnerOptional.get());

        userRepository.save(user);
        foundOwnerOptional.get().setUser(user);
        ownerRepository.save(foundOwnerOptional.get());

        return UserDtoMapping.asDto(user);
    }

    public UserDto updateUser(UpdateUserModel updateUserModel) {
        if (updateUserModel == null){
            throw new CatsLabServiceException("adminModel is null while creating User.");
        }

        if (Strings.isEmpty(updateUserModel.newUsername())) {
            throw new CatsLabServiceException("New username is incorrect while updeting User.");
        }

        Optional<User> user = userRepository.findByUsername(updateUserModel.username());
        Optional<User> usernew = userRepository.findByUsername(updateUserModel.newUsername());

        if (user.isEmpty()) {
            throw new CatsLabServiceException("User with name " + updateUserModel.username() + " doesn't exists.");
        }
        if (usernew.isPresent()) {
            throw new CatsLabServiceException("User with name " + updateUserModel.username() + " already exists.");
        }

        UserRole userRole;
        try {
            userRole = UserRole.valueOf(updateUserModel.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("User role is incorrect while creating User.");
        }

        user.get().setUsername(updateUserModel.newUsername());
        user.get().setPassword((new BCryptPasswordEncoder(12)).encode(updateUserModel.password()));
        user.get().setRole(userRole);

        userRepository.save(user.get());

        return UserDtoMapping.asDto(user.get());
    }

    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new CatsLabServiceException("Null userId while deleting User.");
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this id while deleting User.");
        }

        if (user.get().getOwner() == null) {
            userRepository.deleteById(user.get().getId());
        } else {
            ownerService.deleteOwner(user.get().getOwner().getId());
        }
    }

    public List<UserDto> findAllUsers(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<UserDto> usersDtoList = new ArrayList<>();

        List<User> foundUsers = new ArrayList<>();
        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundUsers.add(user.get());
        } else {
            foundUsers = userRepository.findAll();
        }

        if (foundUsers == null) {
            return usersDtoList;
        }

        for (User u : foundUsers) {
            usersDtoList.add(UserDtoMapping.asDto(u));
        }

        return usersDtoList;
    }

    public UserDto getUserById(GetUserModel getUserModel) {
        if (getUserModel == null){
            throw new CatsLabServiceException("Null getUserModel.");
        }
        Optional<User> user = userRepository.findByUsername(getUserModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        Optional<User> foundUser = userRepository.findById(getUserModel.userId());
        if (foundUser.isEmpty()) {
            throw new CatsLabServiceException("No user with this id");
        }
        if ((!user.get().getRole().equals(UserRole.ADMIN))
                && (!getUserModel.username().equals(foundUser.get().getUsername()))) {
            throw new CatsLabServiceException("Trying to get info about not auth user.");
        }

        return UserDtoMapping.asDto(foundUser.get());
    }

    private boolean checkUsersEqualty(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return !user.isEmpty();
    }

    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
