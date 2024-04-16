package com.mpolina.cats.services.implementations;

import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.dtomapping.CatDtoMapping;
import com.mpolina.cats.dtomapping.OwnerDtoMapping;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.exception.CatsLabServiceException;
import com.mpolina.cats.repository.CatJpaRepository;
import com.mpolina.cats.repository.OwnerJpaRepository;
import com.mpolina.cats.services.OwnerService;
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

    private final CatJpaRepository catRepository;

    private final CatServiceImpl catService;

    @Autowired
    public OwnerServiceImpl(OwnerJpaRepository ownerRepository,
                            CatJpaRepository catRepository,
                            CatServiceImpl catService) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
        this.catService = catService;
    }

    public OwnerDto createOwner(String name, String surname, Date birthday) {
        if (!Strings.isNotEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while creating Owner.");
        }
        if (!Strings.isNotEmpty(surname)) {
            throw new CatsLabServiceException("Surname is incorrect while creating Owner.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while creating Owner.");
        }

        Owner owner = new Owner(name,surname, birthday);
        ownerRepository.save(owner);

        return OwnerDtoMapping.asDto(owner);
    }

    public OwnerDto getOwnerById(Long ownerId) {
        if (ownerId == null) {
            throw new CatsLabServiceException("Id is null while finding Owner.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(ownerId);
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id.");
        }
        return OwnerDtoMapping.asDto(foundOwner.get());
    }

    public OwnerDto updateOwnerInformation(Long ownerId, String name, String surname, Date birthday) {
        if (ownerId == null) {
            throw new CatsLabServiceException("Null ownerId while updating owner info.");
        }
        if (Strings.isEmpty(name)) {
            throw new CatsLabServiceException("Name is incorrect while updating Owner.");
        }
        if (Strings.isEmpty(surname)) {
            throw new CatsLabServiceException("Surname is incorrect while updating Owner.");
        }
        if (birthday == null) {
            throw new CatsLabServiceException("Birthday is null while updating Owner.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(ownerId);
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No owner with this Id while updating Owner info.");
        }

        Owner owner = foundOwner.get();
        owner.setName(name);
        owner.setSurname(surname);
        owner.setBirthday(birthday);
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

        for (Cat cat: foundOwner.getCatsList()){
            catService.deleteCat(cat.getId());
        }

        ownerRepository.deleteById(ownerId);
    }

    public List<OwnerDto> findAllOwners() {
        List<OwnerDto> ownersDtoList = new ArrayList<>();

        List<Owner> foundOwners = ownerRepository.findAll();
        if (foundOwners == null) {
            return ownersDtoList;
        }

        for (Owner owner : foundOwners) {
            ownersDtoList.add(OwnerDtoMapping.asDto(owner));
        }

        return ownersDtoList;
    }

    public List<CatDto> getCats(Long ownerId) {
        List<CatDto> catsDtoList = new ArrayList<>();

        if (ownerId == null) {
            throw new CatsLabServiceException("Null ownerId while getting cats list.");
        }

        Optional<Owner> foundOwner = ownerRepository.findById(ownerId);
        if (!foundOwner.isPresent()) {
            throw new CatsLabServiceException("No Owner with this id.");
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