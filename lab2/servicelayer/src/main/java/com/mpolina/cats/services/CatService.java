package com.mpolina.cats.services;

import com.mpolina.cats.dao.CatDao;
import com.mpolina.cats.dao.OwnerDao;
import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dtomapping.CatDtoMapping;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.cat.CatColor;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.exceptions.CatsLabServiceException;
import lombok.Getter;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Predicate;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CatService {
    @Getter
    private CatDao catDao;

    @Getter
    private OwnerDao ownerDao;

    public CatService(CatDao catDao, OwnerDao ownerDao) {
        if (catDao == null || ownerDao == null) {
            throw new CatsLabServiceException("Null dao while creating cat service.");
        }

        this.catDao = catDao;
        this.ownerDao = ownerDao;
    }

    public CatDto findCatById(Long catId){
        if (catId == null){
            return null;
        }

        Cat foundCat = catDao.findById(catId);
        if(foundCat == null){
            return null;
        }

        return CatDtoMapping.asDto(foundCat);
    }

    public boolean checkCatPresence(Long catId){
        CatDto catDto = findCatById(catId);

        return catDto != null;
    }

    public List<CatDto> getAllCats(){
        List<Cat> cats = catDao.getAll();
        List<CatDto> catsDtoList = new ArrayList<>();

        for (Cat cat: cats) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public List<String> getCatColors(){
        List<String> catColorsList = Stream.of(CatColor.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return  catColorsList;
    }

    public List<CatDto> getCatsByPredicate(Predicate<Cat> predicate){
        List<Cat> cats = catDao.getAll();
        List<Cat> chosenCatsList = cats.stream().filter(predicate).toList();
        List<CatDto> catsDtoList = new ArrayList<>();

        for (Cat cat: chosenCatsList) {
            catsDtoList.add(CatDtoMapping.asDto(cat));
        }

        return catsDtoList;
    }

    public CatDto createCat(String name, Date birthday, String breed, String strCatColor, Long ownerId) {
        if (Strings.isNullOrEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while creating Cat.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while creating Cat.");
        }
        if (Strings.isNullOrEmpty(breed)) {
            throw new CatsLabServiceException("Breed is incorrect while creating Cat.");
        }
        if (Strings.isNullOrEmpty(strCatColor)) {
            throw new CatsLabServiceException("Cat color is incorrect while creating Cat.");
        }

        CatColor catColor;
        try{
            catColor = CatColor.valueOf(strCatColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while creating Cat.");
        }

        if (ownerId == null){
            throw new CatsLabServiceException("Null ownerId while creating Cat.");
        }

        Owner foundOwner = ownerDao.findById(ownerId);
        if(ownerDao.findById(ownerId) == null){
            throw new CatsLabServiceException("No owner with this Id while creating Cat.");
        }

        Cat cat = new Cat(name, birthday, breed, catColor, foundOwner);
        catDao.save(cat);

        return CatDtoMapping.asDto(cat);
    }

    public CatDto updateCatInformation(Long catId, String name, Date birthday, String breed, String strCatColor, Long ownerId) {
        if (Strings.isNullOrEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while updating Cat info.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while updating Cat info.");
        }
        if (Strings.isNullOrEmpty(breed)) {
            throw new CatsLabServiceException("Breed is incorrect while updating Cat info.");
        }
        if (Strings.isNullOrEmpty(strCatColor)) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        if (catId == null){
            throw new CatsLabServiceException("Null catId while updating Cat info.");
        }

        Cat foundCat = catDao.findById(catId);
        if(foundCat == null){
            throw new CatsLabServiceException("No cat with this Id while updating Cat info.");
        }

        CatColor catColor;
        try{
            catColor = CatColor.valueOf(strCatColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CatsLabServiceException("Cat color is incorrect while updating Cat info.");
        }

        if (ownerId == null){
            throw new CatsLabServiceException("Null ownerId while updating Cat info.");
        }

        Owner foundOwner = ownerDao.findById(ownerId);
        if(foundOwner == null){
            throw new CatsLabServiceException("No owner with this Id while updating Cat info.");
        }

        foundCat.setName(name);
        foundCat.setBirthday(birthday);
        foundCat.setBreed(breed);
        foundCat.setColor(catColor);
        foundCat.setOwner(foundOwner);
        catDao.update(foundCat);

        return CatDtoMapping.asDto(foundCat);
    }

    public void deleteCat(Long catId){
        if (catId == null){
            throw new CatsLabServiceException("Null catId while deleting Cat.");
        }

        Cat foundCat = catDao.findById(catId);

        if(foundCat == null){
            throw new CatsLabServiceException("No cat with this Id while deleting Cat.");
        }

        catDao.delete(foundCat);
    }
}
