package com.mpolina.cats.services.implementations;

import com.mpolina.cats.dto.AdminDto;
import com.mpolina.cats.dto.UserDto;
import com.mpolina.cats.dtomapping.AdminDtoMapping;
import com.mpolina.cats.dtomapping.UserDtoMapping;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.entity.user.User;
import com.mpolina.cats.entity.user.UserRole;
import com.mpolina.cats.exception.CatsLabServiceException;
import com.mpolina.cats.repository.OwnerJpaRepository;
import com.mpolina.cats.repository.UserJpaRepository;
import com.mpolina.cats.services.OwnerService;
import com.mpolina.cats.services.UserService;
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

    public AdminDto createAdmin(String username, String password) {
        if (Strings.isEmpty(username)) {
            throw new CatsLabServiceException("Username is incorrect while creating User.");
        }
        if (Strings.isEmpty(password)) {
            throw new CatsLabServiceException("Password is incorrect while creating User.");
        }

        if (checkUsersEqualty(username) == true) {
            throw new CatsLabServiceException("User with name " + username + " already exists.");
        }

        User user = new User(username, passwordEncoder().encode(password), UserRole.ADMIN, null);

        userRepository.save(user);

        return AdminDtoMapping.asDto(user);
    }

    public UserDto createUser(String username, String password, Long ownerId) {
        if (Strings.isEmpty(username)) {
            throw new CatsLabServiceException("Username is incorrect while creating User.");
        }
        if (Strings.isEmpty(password)) {
            throw new CatsLabServiceException("Password is incorrect while creating User.");
        }

        if (checkUsersEqualty(username) == true) {
            throw new CatsLabServiceException("User with name " + username + " already exists.");
        }
        if (ownerId == null) {
            throw new CatsLabServiceException("Null ownerId while creating User.");
        }

        Optional<Owner> foundOwnerOptional = ownerRepository.findById(ownerId);
        if (!foundOwnerOptional.isPresent()) {
            throw new CatsLabServiceException("No owner with this Id while creating User.");
        }

        User user = new User(username, passwordEncoder().encode(password), UserRole.USER, foundOwnerOptional.get());

        userRepository.save(user);
        foundOwnerOptional.get().setUser(user);
        ownerRepository.save(foundOwnerOptional.get());

        return UserDtoMapping.asDto(user);
    }

    public UserDto updateUser(String username, String newUsername, String password, String role) {
        if (Strings.isEmpty(newUsername)) {
            throw new CatsLabServiceException("New username is incorrect while updeting User.");
        }

        Optional<User> user = userRepository.findByUsername(username);
        Optional<User> usernew = userRepository.findByUsername(newUsername);

        if (user.isEmpty()) {
            throw new CatsLabServiceException("User with name " + username + " doesn't exists.");
        }
        if (usernew.isPresent()) {
            throw new CatsLabServiceException("User with name " + username + " already exists.");
        }

        UserRole userRole;
        try {
            userRole = UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("User role is incorrect while creating User.");
        }

        user.get().setUsername(newUsername);
        user.get().setPassword((new BCryptPasswordEncoder(12)).encode(password));
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

    public UserDto getUserById(Long userId, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new CatsLabServiceException("No user with this id");
        }
        if ((!user.get().getRole().equals(UserRole.ADMIN))
                && (!username.equals(foundUser.get().getUsername()))) {
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
//
//    public String getOwnersUser(Long ownerId) {
//        return userRepository.findByOwnerId(ownerId).getPassword();
//    }
//
//    public OwnerDto getOwner(Long userId){
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()){
//            throw new CatsLabServiceException("No user with this id.");
//        }
//        return OwnerDtoMapping.asDto(userOptional.get().getOwner());
//    }
}
