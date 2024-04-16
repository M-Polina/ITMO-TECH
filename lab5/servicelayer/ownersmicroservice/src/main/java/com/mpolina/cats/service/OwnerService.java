package com.mpolina.cats.service;


import com.mpolina.cats.getdtos.CatDto;
import com.mpolina.cats.getdtos.OwnerDto;
import com.mpolina.cats.requestmodels.owner.GetOwnerModel;
import com.mpolina.cats.requestmodels.owner.OwnerModel;
import com.mpolina.cats.requestmodels.owner.UpdateOwnerModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface OwnerService {
    OwnerDto createOwner(OwnerModel ownerModel);

    OwnerDto getOwnerById(GetOwnerModel getOwnerModel);

    OwnerDto updateOwnerInformation(UpdateOwnerModel updateOwnerModel);

    void deleteOwner(Long ownerId);

    List<OwnerDto> findAllOwners(String username);

    List<OwnerDto> findOwnersByName(String name);

    List<OwnerDto> findOwnersBySurname(String surname);

    List<OwnerDto> findOwnersByBirthday(Date birthday);

    List<CatDto> getCats(GetOwnerModel getOwnerModel);
}