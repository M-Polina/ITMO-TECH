package com.mpolina.cats.services;


import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface OwnerService {
    OwnerDto createOwner(String name, String surname, Date birthday);

    OwnerDto getOwnerById(Long ownerId);

    OwnerDto updateOwnerInformation(Long ownerId, String name, String surname, Date birthday);

    void deleteOwner(Long ownerId);

    List<OwnerDto> findAllOwners();

    List<OwnerDto> findOwnersByName(String name);

    List<OwnerDto> findOwnersBySurname(String surname);

    List<OwnerDto> findOwnersByBirthday(Date birthday);

    List<CatDto> getCats(Long ownerId);
}