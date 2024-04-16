package com.mpolina.cats.services;

import com.mpolina.cats.dao.CatDao;
import com.mpolina.cats.dao.OwnerDao;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.dtomapping.OwnerDtoMapping;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.exceptions.CatsLabServiceException;
import lombok.Getter;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Predicate;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OwnerService {
    @Getter
    private CatDao catDao;

    @Getter
    private OwnerDao ownerDao;

    public OwnerService(CatDao catDao, OwnerDao ownerDao) {
        if (catDao == null || ownerDao == null) {
            throw new CatsLabServiceException("Null dao while creating owner service.");
        }

        this.catDao = catDao;
        this.ownerDao = ownerDao;
    }

    public OwnerDto findOwnerById(Long ownerId) {
        if (ownerId == null) {
            return null;
        }

        Owner foundOwner = ownerDao.findById(ownerId);
        if (foundOwner == null) {
            return null;
        }

        return OwnerDtoMapping.asDto(foundOwner);
    }

    public boolean checkOwnerPresence(Long ownerId){
        OwnerDto ownerDto = findOwnerById(ownerId);

        return ownerDto != null;
    }

    public List<OwnerDto> getAllOwners() {
        List<Owner> owners = ownerDao.getAll();
        List<OwnerDto> ownersDtoList = new ArrayList<>();

        for (Owner owner : owners) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }

    public List<OwnerDto> getOwnersByOwnerPredicate(Predicate<Owner> predicate) {
        List<Owner> owners = ownerDao.getAll();
        List<Owner> chosenOwnersList = owners.stream().filter(predicate).toList();
        List<OwnerDto> ownersDtoList = new ArrayList<>();

        for (Owner owner : chosenOwnersList) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }

    public OwnerDto createOwner(String name, String surname, Date birthday) {
        if (Strings.isNullOrEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while creating Owner.");
        }
        if (Strings.isNullOrEmpty(surname)) {
            throw new CatsLabServiceException("Name is incorrect while creating Owner.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while creating Owner.");
        }

        Owner owner = new Owner(name, surname, birthday);
        ownerDao.save(owner);

        return OwnerDtoMapping.asDto(owner);
    }

    public OwnerDto updateOwnerInformation(Long ownerId, String name, String surname, Date birthday) {
        if (Strings.isNullOrEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while creating Owner.");
        }
        if (Strings.isNullOrEmpty(surname)) {
            throw new CatsLabServiceException("Name is incorrect while creating Owner.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while creating Owner.");
        }

        if (ownerId == null){
            throw new CatsLabServiceException("Null catId while updating Owner info.");
        }

        Owner foundOwner = ownerDao.findById(ownerId);
        if(foundOwner == null){
            throw new CatsLabServiceException("No cat with this Id while updating Owner info.");
        }

        foundOwner.setName(name);
        foundOwner.setSurname(surname);
        foundOwner.setBirthday(birthday);
        ownerDao.update(foundOwner);

        return OwnerDtoMapping.asDto(foundOwner);
    }

    public void deleteOwner(Long ownerId){
        if (ownerId == null){
            throw new CatsLabServiceException("Null catId while deleting Owner.");
        }

        Owner foundOwner = ownerDao.findById(ownerId);

        if(foundOwner == null){
            throw new CatsLabServiceException("No cat with this Id while deleting Owner.");
        }

        ownerDao.delete(foundOwner);
    }
}