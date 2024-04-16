package com.mpolina.cats.services.implementations;


import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.dtomapping.CatDtoMapping;
import com.mpolina.cats.dtomapping.OwnerDtoMapping;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.cat.CatColor;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.entity.user.User;
import com.mpolina.cats.entity.user.UserRole;
import com.mpolina.cats.exception.CatsLabServiceException;
import com.mpolina.cats.repository.CatJpaRepository;
import com.mpolina.cats.repository.OwnerJpaRepository;
import com.mpolina.cats.repository.UserJpaRepository;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.CatsFriendsService;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CatServiceImpl implements CatService {
    private CatJpaRepository catRepository;
    private OwnerJpaRepository ownerRepository;
    private UserJpaRepository userRepository;
    private final CatsFriendsService catsFriendsService;

    @Autowired
    public CatServiceImpl(OwnerJpaRepository ownerRepository,
                          CatJpaRepository catRepository,
                          UserJpaRepository userRepository,
                          CatsFriendsService catsFriendsService) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
        this.catsFriendsService = catsFriendsService;
        this.userRepository = userRepository;
    }

    public CatDto getCatById(Long catId, String username) {
        if (catId == null) {
            throw new CatsLabServiceException("Id is null while finding Cat.");
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }


        Optional<Cat> foundCat = catRepository.findById(catId);
        if (!foundCat.isPresent()) {
            throw new CatsLabServiceException("No Cat with this id.");
        }
        if ((!user.get().getRole().equals(UserRole.ADMIN)
                && user.get().getOwner().getId() != foundCat.get().getOwner().getId())) {
            throw new CatsLabServiceException("User(owner) doesn't have cat with this id.");
        }

        return CatDtoMapping.asDto(foundCat.get());
    }

    public List<CatDto> findAllCats(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        List<Cat> foundCats = new ArrayList<>();
        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundCats = user.get().getOwner().getCatsList();
        } else {
            foundCats = catRepository.findAll();
        }

        if (foundCats == null) {
            return catsDtoList;
        }

        for (Cat cat : foundCats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public List<String> getCatColors() {
        List<String> catColorsList = Stream.of(CatColor.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return catColorsList;
    }

    public CatDto createCat(String name, Date birthday, String breed, String strCatColor, Long ownerId) {
        if (!Strings.isNotEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while creating Cat.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while creating Cat.");
        }
        if (!Strings.isNotEmpty(breed)) {
            throw new CatsLabServiceException("Breed is incorrect while creating Cat.");
        }

        if (ownerId == null) {
            throw new CatsLabServiceException("Null ownerId while creating Cat.");
        }

        CatColor catColor;
        try {
            catColor = CatColor.valueOf(strCatColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while creating Cat.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(ownerId);
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id while creating cat.");
        }

        Cat cat = new Cat(name, birthday, breed, catColor, foundOwner.get());
        catRepository.save(cat);

        return CatDtoMapping.asDto(cat);
    }

    public CatDto updateCatInformation(Long catId, String name, Date birthday, String breed, String strCatColor, Long ownerId) {
        if (!Strings.isNotEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while updating Cat info.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while updating Cat info.");
        }
        if (!Strings.isNotEmpty(breed)) {
            throw new CatsLabServiceException("Breed is incorrect while updating Cat info.");
        }
        if (!Strings.isNotEmpty(strCatColor)) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        if (catId == null) {
            throw new CatsLabServiceException("Null catId while updating Cat info.");
        }

        Optional<Cat> foundCatOptional = catRepository.findById(catId);
        if (!foundCatOptional.isPresent()) {
            throw new CatsLabServiceException("No Cat with this id.");
        }
        Cat foundCat = foundCatOptional.get();

        CatColor catColor;
        try {
            catColor = CatColor.valueOf(strCatColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        if (ownerId == null) {
            throw new CatsLabServiceException("Null ownerId while updating Cat info.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(ownerId);
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id.");
        }

        foundCat.setName(name);
        foundCat.setBirthday(birthday);
        foundCat.setBreed(breed);
        foundCat.setColor(catColor);
        foundCat.setOwner(foundOwner.get());

        catRepository.save(foundCat);

        return CatDtoMapping.asDto(foundCat);
    }

    public void deleteCat(Long catId) {
        if (catId == null) {
            throw new CatsLabServiceException("Null catId while deleting Cat.");
        }

        Optional<Cat> foundCatOptional = catRepository.findById(catId);
        if (!foundCatOptional.isPresent()) {
            throw new CatsLabServiceException("No cat with this Id while deleting Cat.");
        }
        Cat foundCat = foundCatOptional.get();

        for (Cat friendCat : foundCat.getFriendCatsList().stream().toList()) {
            catsFriendsService.breakeCatsFriendship(friendCat.getId(), foundCat.getId());
        }

        foundCat.getOwner().getCatsList().removeIf(cati -> (cati.getId() == foundCat.getId()));
        ownerRepository.save(foundCat.getOwner());

        catRepository.delete(foundCat);
    }

    public List<CatDto> findCatsByName(String name, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (!Strings.isNotEmpty(name)) {
            return catsDtoList;
        }

        List<Cat> foundCats = catRepository.findByName(name);
        if (foundCats == null) {
            return catsDtoList;
        }

        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundCats = foundCats.stream()
                    .filter(cat -> cat.getOwner().getId() == user.get().getOwner().getId())
                    .toList();
        }


        for (Cat cat : foundCats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public List<CatDto> findCatsByBirthday(Date birthday, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (birthday == null) {
            return catsDtoList;
        }

        List<Cat> foundCats = catRepository.findByBirthday(birthday);
        if (foundCats == null) {
            return catsDtoList;
        }

        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundCats = foundCats.stream()
                    .filter(cat -> cat.getOwner().getId() == user.get().getOwner().getId())
                    .toList();
        }

        for (Cat cat : foundCats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }
        return catsDtoList;
    }

    public List<CatDto> findCatsByColor(String strCatColor, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        CatColor catColor;
        try {
            catColor = CatColor.valueOf(strCatColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        List<Cat> foundCats = catRepository.findByColor(catColor);
        if (foundCats == null) {
            return catsDtoList;
        }

        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundCats = foundCats.stream()
                    .filter(cat -> cat.getOwner().getId() == user.get().getOwner().getId())
                    .toList();
        }

        for (Cat cat : foundCats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public List<CatDto> findCatsByBreed(String breed, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (!Strings.isNotEmpty(breed)) {
            return catsDtoList;
        }

        List<Cat> foundCats = catRepository.findByBreed(breed);
        if (foundCats == null) {
            return catsDtoList;
        }

        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundCats = foundCats.stream()
                    .filter(cat -> cat.getOwner().getId() == user.get().getOwner().getId())
                    .toList();
        }

        for (Cat cat : foundCats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public OwnerDto getCatOwner(Long catId, String username) {
        if (catId == null) {
            throw new CatsLabServiceException("Null catId while getting Cat's owner.");
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        Optional<Cat> foundCatOptional = catRepository.findById(catId);
        if (!foundCatOptional.isPresent()) {
            throw new CatsLabServiceException("No cat with this Id while getting Cat's owner.");
        }
        Cat foundCat = foundCatOptional.get();

        if ((!user.get().getRole().equals(UserRole.ADMIN)
                && user.get().getOwner().getId() != foundCat.getOwner().getId())) {
            throw new CatsLabServiceException("User (owner) doesn't have cat with this id.");
        }

        return OwnerDtoMapping.asDto(foundCat.getOwner());
    }

}
