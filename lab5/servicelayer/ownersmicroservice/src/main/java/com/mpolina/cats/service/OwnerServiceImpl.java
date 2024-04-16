package com.mpolina.cats.service;

import com.mpolina.cats.dtomapping.CatDtoMapping;
import com.mpolina.cats.dtomapping.OwnerDtoMapping;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.entity.user.User;
import com.mpolina.cats.entity.user.UserRole;
import com.mpolina.cats.exception.CatsLabServiceException;
import com.mpolina.cats.getdtos.CatDto;
import com.mpolina.cats.getdtos.OwnerDto;
import com.mpolina.cats.repository.CatJpaRepository;
import com.mpolina.cats.repository.OwnerJpaRepository;
import com.mpolina.cats.repository.UserJpaRepository;
import com.mpolina.cats.requestmodels.cat.FriendCatsModel;
import com.mpolina.cats.requestmodels.owner.GetOwnerModel;
import com.mpolina.cats.requestmodels.owner.OwnerModel;
import com.mpolina.cats.requestmodels.owner.UpdateOwnerModel;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerJpaRepository ownerRepository;
    private final UserJpaRepository userRepository;
    private final CatJpaRepository catRepository;

    @Autowired
    public OwnerServiceImpl(OwnerJpaRepository ownerRepository,
                            UserJpaRepository userRepository,
                            CatJpaRepository catRepository) {
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
        this.catRepository = catRepository;
    }

    public OwnerDto createOwner(OwnerModel ownerModel) {
        if (ownerModel == null){
            throw new CatsLabServiceException("ownerModel is null.");
        }
        if (!Strings.isNotEmpty(ownerModel.name())) {
            throw new CatsLabServiceException("Name is incorrect while creating Owner.");
        }
        if (!Strings.isNotEmpty(ownerModel.surname())) {
            throw new CatsLabServiceException("Surname is incorrect while creating Owner.");
        }
        if (ownerModel.birthday() == null) {
            throw new CatsLabServiceException("Birthday is null while creating Owner.");
        }

        Owner owner = new Owner(ownerModel.name(), ownerModel.surname(), ownerModel.birthday());
        ownerRepository.save(owner);

        return OwnerDtoMapping.asDto(owner);
    }

    public OwnerDto getOwnerById(GetOwnerModel getOwnerModel) {
        if (getOwnerModel == null){
            throw new CatsLabServiceException("getOwnerModel is null.");
        }
        if (getOwnerModel.ownerId() == null) {
            throw new CatsLabServiceException("Id is null while finding Owner.");
        }

        Optional<User> user = userRepository.findByUsername(getOwnerModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(getOwnerModel.ownerId());
        if (foundOwner.isEmpty()) {
            throw new CatsLabServiceException("No Owner with this id.");
        }

        if ((!user.get().getRole().equals(UserRole.ADMIN)
        && user.get().getId() != foundOwner.get().getUser().getId())){
            throw new CatsLabServiceException("Owner with this id is not owned by this user.");
        }

        return OwnerDtoMapping.asDto(foundOwner.get());
    }

    public OwnerDto updateOwnerInformation(UpdateOwnerModel updateOwnerModel) {
        if (updateOwnerModel == null){
            throw new CatsLabServiceException("getOwnerModel is null.");
        }
        if (updateOwnerModel.ownerId() == null) {
            throw new CatsLabServiceException("Null ownerId while updating owner info.");
        }
        if (Strings.isEmpty(updateOwnerModel.ownerModel().name())) {
            throw new CatsLabServiceException("Name is incorrect while updating Owner.");
        }
        if (Strings.isEmpty(updateOwnerModel.ownerModel().surname())) {
            throw new CatsLabServiceException("Surname is incorrect while updating Owner.");
        }
        if (updateOwnerModel.ownerModel().birthday() == null) {
            throw new CatsLabServiceException("Birthday is null while updating Owner.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(updateOwnerModel.ownerId());
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No owner with this Id while updating Owner info.");
        }

        Owner owner = foundOwner.get();
        owner.setName(updateOwnerModel.ownerModel().name());
        owner.setSurname(updateOwnerModel.ownerModel().name());
        owner.setBirthday(updateOwnerModel.ownerModel().birthday());
        ownerRepository.saveAndFlush(owner);

        return OwnerDtoMapping.asDto(foundOwner.get());
    }

    public void deleteOwner(Long ownerId) {
        if (ownerId == null) {
            throw new CatsLabServiceException("Null ownerId while deleting  owner.");
        }

        Optional<Owner> foundOwnerOptional = ownerRepository.findById(ownerId);
        if (!foundOwnerOptional.isPresent()) {
            throw new CatsLabServiceException("No owner with this Id while deleting Owner.");
        }
        Owner foundOwner = foundOwnerOptional.get();

        for (Cat cat : foundOwner.getCatsList().stream().toList()) {

            for (Cat friendCat : cat.getFriendCatsList().stream().toList()) {
                cat.getFriendCatsList().removeIf(c -> (c.getId() == friendCat.getId()));
                friendCat.getFriendCatsList().removeIf(c -> (c.getId() == cat.getId()));

                catRepository.saveAndFlush(cat);
                catRepository.saveAndFlush(friendCat);
            }

            cat.getOwner().getCatsList().removeIf(cati -> (cati.getId() == cat.getId()));
            ownerRepository.save(cat.getOwner());

            catRepository.delete(cat);
        }

        ownerRepository.deleteById(ownerId);
    }

    public List<OwnerDto> findAllOwners(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<OwnerDto> ownersDtoList = new ArrayList<>();

        List<Owner> foundOwners = new ArrayList<>();
        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            foundOwners.add(user.get().getOwner());
        } else {
            foundOwners = ownerRepository.findAll();
        }

        if (foundOwners == null) {
            return ownersDtoList;
        }

        for (Owner owner : foundOwners) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }

    public List<CatDto> getCats(GetOwnerModel ownerModel) {
        if (ownerModel == null){
            throw new CatsLabServiceException("Nukk ownerModel.");
        }
        Optional<User> user = userRepository.findByUsername(ownerModel.username());
        if (user.isEmpty()) {
            throw new CatsLabServiceException("No user with this username.");
        }

        List<CatDto> catsDtoList = new ArrayList<>();

        if (ownerModel.ownerId() == null) {
            throw new CatsLabServiceException("Null ownerId while getting cats list.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(ownerModel.ownerId());
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id.");
        }

        if ((!user.get().getRole().equals(UserRole.ADMIN)
                && user.get().getId() != foundOwner.get().getUser().getId())){
            throw new CatsLabServiceException("Owner with this id is not owned by this user.");
        }

        List<Cat> foundCats = foundOwner.get().getCatsList();
        if (foundCats == null) {
            return catsDtoList;
        }

        for (Cat cat : foundCats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public List<OwnerDto> findOwnersByName(String name) {
        List<OwnerDto> ownersDtoList = new ArrayList<>();

        if (!Strings.isNotEmpty(name)) {
            return ownersDtoList;
        }

        List<Owner> foundOwners = ownerRepository.findByName(name);
        if (foundOwners == null) {
            return ownersDtoList;
        }

        for (Owner owner : foundOwners) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }

    public List<OwnerDto> findOwnersBySurname(String surname) {
        List<OwnerDto> ownersDtoList = new ArrayList<>();

        if (!Strings.isNotEmpty(surname)) {
            return ownersDtoList;
        }

        List<Owner> foundOwners = ownerRepository.findBySurname(surname);
        if (foundOwners == null) {
            return ownersDtoList;
        }

        for (Owner owner : foundOwners) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }

    public List<OwnerDto> findOwnersByBirthday(Date birthday) {
        List<OwnerDto> ownersDtoList = new ArrayList<>();

        if (birthday == null) {
            return ownersDtoList;
        }

        List<Owner> foundOwners = ownerRepository.findByBirthday(birthday);
        if (foundOwners == null) {
            return ownersDtoList;
        }

        for (Owner owner : foundOwners) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }
}