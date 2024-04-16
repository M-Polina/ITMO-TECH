package com.mpolina.cats.service;


import com.mpolina.cats.getdtos.CatDto;
import com.mpolina.cats.getdtos.OwnerDto;
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
import com.mpolina.cats.requestmodels.cat.*;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public CatServiceImpl(OwnerJpaRepository ownerRepository,
                          CatJpaRepository catRepository,
                          UserJpaRepository userRepository) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
        this.userRepository = userRepository;
    }

    public CatDto getCatById(GetCatModel getCatModel) {
        if (getCatModel == null){
            throw new CatsLabServiceException("Null getCatModel.");
        }
        if (getCatModel.catId() == null) {
            throw new CatsLabServiceException("Id is null while finding Cat.");
        }

        Optional<User> user = userRepository.findByUsername(getCatModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }


        Optional<Cat> foundCat = catRepository.findById(getCatModel.catId());
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

    public void createCat(CatModel catModel) {
        if (catModel == null){
            throw new CatsLabServiceException("Null catModel.");
        }
        if (!Strings.isNotEmpty(catModel.name())) {
            throw new CatsLabServiceException("Name is incorrect while creating Cat.");
        }
        if (catModel.birthday() == null) {
            throw new CatsLabServiceException("Birthday is null while creating Cat.");
        }
        if (!Strings.isNotEmpty(catModel.breed())) {
            throw new CatsLabServiceException("Breed is incorrect while creating Cat.");
        }

        if (catModel.ownerId() == null) {
            throw new CatsLabServiceException("Null ownerId while creating Cat.");
        }

        CatColor catColor;
        try {
            catColor = CatColor.valueOf(catModel.color().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while creating Cat.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(catModel.ownerId());
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id while creating cat.");
        }

        Cat cat = new Cat(catModel.name(), catModel.birthday(), catModel.breed(), catColor, foundOwner.get());
        catRepository.save(cat);
    }

    public void updateCatInformation(UpdateCatModel updateCatModel) {
        if (updateCatModel == null){
            throw new CatsLabServiceException("Null updateCatModel.");
        }
        if (!Strings.isNotEmpty(updateCatModel.catModel().name())) {
            throw new CatsLabServiceException("Name is incorrect while updating Cat info.");
        }
        if (updateCatModel.catModel().birthday() == null) {
            throw new CatsLabServiceException("Birthday is null while updating Cat info.");
        }
        if (!Strings.isNotEmpty(updateCatModel.catModel().breed())) {
            throw new CatsLabServiceException("Breed is incorrect while updating Cat info.");
        }
        if (!Strings.isNotEmpty(updateCatModel.catModel().color())) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        if (updateCatModel.catId() == null) {
            throw new CatsLabServiceException("Null catId while updating Cat info.");
        }

        Optional<Cat> foundCatOptional = catRepository.findById(updateCatModel.catId());
        if (!foundCatOptional.isPresent()) {
            throw new CatsLabServiceException("No Cat with this id.");
        }
        Cat foundCat = foundCatOptional.get();

        CatColor catColor;
        try {
            catColor = CatColor.valueOf(updateCatModel.catModel().color().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        if (updateCatModel.catModel().ownerId() == null) {
            throw new CatsLabServiceException("Null ownerId while updating Cat info.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(updateCatModel.catModel().ownerId());
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id.");
        }

        foundCat.setName(updateCatModel.catModel().name());
        foundCat.setBirthday(updateCatModel.catModel().birthday());
        foundCat.setBreed(updateCatModel.catModel().breed());
        foundCat.setColor(catColor);
        foundCat.setOwner(foundOwner.get());

        catRepository.save(foundCat);
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
            breakeCatsFriendship(new FriendCatsModel(friendCat.getId(), foundCat.getId()));
        }

        foundCat.getOwner().getCatsList().removeIf(cati -> (cati.getId() == foundCat.getId()));
        ownerRepository.save(foundCat.getOwner());

        catRepository.delete(foundCat);
    }

    public List<CatDto> findCatsByName(FindCatByParameterModel findCatByParameterModel) {
        if (findCatByParameterModel == null){
            throw new CatsLabServiceException("Null findCatByParameterModel.");
        }
        Optional<User> user = userRepository.findByUsername(findCatByParameterModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (!Strings.isNotEmpty(findCatByParameterModel.name())) {
            return catsDtoList;
        }

        List<Cat> foundCats = catRepository.findByName(findCatByParameterModel.name());
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

    public List<CatDto> findCatsByBirthday(FindCatByParameterModel findCatByParameterModel) {
        if (findCatByParameterModel == null){
            throw new CatsLabServiceException("Null findCatByParameterModel.");
        }
        Optional<User> user = userRepository.findByUsername(findCatByParameterModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (findCatByParameterModel.birthday() == null) {
            return catsDtoList;
        }

        List<Cat> foundCats = catRepository.findByBirthday(findCatByParameterModel.birthday());
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

    public List<CatDto> findCatsByColor(FindCatByParameterModel findCatByParameterModel) {
        if (findCatByParameterModel == null){
            throw new CatsLabServiceException("Null findCatByParameterModel.");
        }
        Optional<User> user = userRepository.findByUsername(findCatByParameterModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        CatColor catColor;
        try {
            catColor = CatColor.valueOf(findCatByParameterModel.color().toUpperCase());
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

    public List<CatDto> findCatsByBreed(FindCatByParameterModel findCatByParameterModel) {
        if (findCatByParameterModel == null){
            throw new CatsLabServiceException("Null findCatByParameterModel.");
        }
        Optional<User> user = userRepository.findByUsername(findCatByParameterModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (!Strings.isNotEmpty(findCatByParameterModel.breed())) {
            return catsDtoList;
        }

        List<Cat> foundCats = catRepository.findByBreed(findCatByParameterModel.breed());
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

    public OwnerDto getCatOwner(GetCatModel getCatModel) {
        if (getCatModel == null){
            throw new CatsLabServiceException("Null getCatModel.");
        }
        if (getCatModel.catId() == null) {
            throw new CatsLabServiceException("Null catId while getting Cat's owner.");
        }

        Optional<User> user = userRepository.findByUsername(getCatModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        Optional<Cat> foundCatOptional = catRepository.findById(getCatModel.catId());
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

    public boolean checkCatsFriendshipPresence(FriendCatsModel friendCatsModel) {
        if (friendCatsModel == null){
            throw new CatsLabServiceException("Null friendCatsModel.");
        }

        Optional<Cat> foundCatOptional1 = catRepository.findById(friendCatsModel.catId1());
        if (!foundCatOptional1.isPresent()) {
            return false;
        }

        Optional<Cat> foundCatOptional2 = catRepository.findById(friendCatsModel.catId2());
        if (!foundCatOptional2.isPresent()) {
            return false;
        }

        boolean cat1HasCat2AsFriend = foundCatOptional1.get().getFriendCatsList().stream().anyMatch(c -> c.getId() == friendCatsModel.catId2());
        boolean cat2HasCat1AsFriend = foundCatOptional2.get().getFriendCatsList().stream().anyMatch(c -> c.getId() == friendCatsModel.catId1());

        if (cat1HasCat2AsFriend && cat2HasCat1AsFriend) {
            return true;
        }

        return false;
    }

    public void makeCatsFriends(FriendCatsModel friendCatsModel) {
        if (friendCatsModel == null){
            throw new CatsLabServiceException("Null friendCatsModel.");
        }

        Optional<Cat> foundCatOptional1 = catRepository.findById(friendCatsModel.catId1());
        if (!foundCatOptional1.isPresent()) {
            throw new CatsLabServiceException("No cat with this Id while making cats friends.");
        }
        Cat foundCat1 = foundCatOptional1.get();

        Optional<Cat> foundCatOptional2 = catRepository.findById(friendCatsModel.catId2());
        if (!foundCatOptional2.isPresent()) {
            throw new CatsLabServiceException("No cat with this Id while making cats friends");
        }
        Cat foundCat2 = foundCatOptional2.get();

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == friendCatsModel.catId2());
        boolean cat2HasCat1AsFriend = foundCat2.getFriendCatsList().stream().anyMatch(c -> c.getId() == friendCatsModel.catId1());

        if (cat1HasCat2AsFriend && cat2HasCat1AsFriend) {
            throw new CatsLabServiceException("Cats already are friends while making cats friends.");
        }

        if (cat1HasCat2AsFriend || cat2HasCat1AsFriend) {
            throw new CatsLabServiceException("Cats are not mutual friends while making cats friends.");
        }

        foundCat1.getFriendCatsList().add(foundCat2);
        foundCat2.getFriendCatsList().add(foundCat1);

        catRepository.saveAndFlush(foundCat1);
        catRepository.saveAndFlush(foundCat2);
    }

    public void breakeCatsFriendship(FriendCatsModel friendCatsModel) {
        if (friendCatsModel == null){
            throw new CatsLabServiceException("Null friendCatsModel.");
        }
        if (friendCatsModel.catId1() == null || friendCatsModel.catId2() == null) {
            throw new CatsLabServiceException("Null id while braking cats friendship.");
        }

        Optional<Cat> foundCatOptional1 = catRepository.findById(friendCatsModel.catId1());
        if (!foundCatOptional1.isPresent()) {
            throw new CatsLabServiceException("No cat with this first Id while braking cats friendship.");
        }
        Cat foundCat1 = foundCatOptional1.get();

        Optional<Cat> foundCatOptional2 = catRepository.findById(friendCatsModel.catId2());
        if (!foundCatOptional2.isPresent()) {
            throw new CatsLabServiceException("No cat with this second Id while braking cats friendship.");
        }
        Cat foundCat2 = foundCatOptional2.get();

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == friendCatsModel.catId2());
        boolean cat2HasCat1AsFriend = foundCat2.getFriendCatsList().stream().anyMatch(c -> c.getId() == friendCatsModel.catId1());

        if ((!cat1HasCat2AsFriend) || (!cat2HasCat1AsFriend)) {
            throw new CatsLabServiceException("Cats are not friends  while braking cats friendship.");
        }

        if (!cat1HasCat2AsFriend) {
            throw new CatsLabServiceException("First cat hasn't got second as friend  while braking cats friendship.");
        }

        if (!cat2HasCat1AsFriend) {
            throw new CatsLabServiceException("Second cat hasn't got first as friend while braking cats friendship.");
        }

        foundCat1.getFriendCatsList().removeIf(c -> (c.getId() == foundCat2.getId()));
        foundCat2.getFriendCatsList().removeIf(c -> (c.getId() == foundCat1.getId()));

        catRepository.saveAndFlush(foundCat1);
        catRepository.saveAndFlush(foundCat2);
    }
}
