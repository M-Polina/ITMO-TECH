package com.mpolina.cats.controllers;

import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.services.OwnerService;

import java.sql.Date;
import java.util.List;

public class OwnerController {
    private OwnerService ownerService;

    public OwnerController(OwnerService ownerService){
        this.ownerService = ownerService;
    }
    public OwnerDto createOwner(String name, String surname, Date birthday){
        OwnerDto ownerDto = ownerService.createOwner(name, surname, birthday);
        return ownerDto;
    }

    public OwnerDto findOwnerById(Long ownerId){
        OwnerDto ownerDto = ownerService.findOwnerById(ownerId);
        return ownerDto;
    }

    public List<OwnerDto> getAllOwners(){
        List<OwnerDto> ownerDtoList = ownerService.getAllOwners();
        return ownerDtoList;
    }

    public OwnerDto updateOwnerInformation(Long ownerId, String name, String surname, Date birthday) {
        OwnerDto ownerDto = ownerService.updateOwnerInformation(ownerId, name, surname, birthday);
        return ownerDto;
    }

    public boolean deleteOwner(Long ownerId){
        try {
            ownerService.deleteOwner(ownerId);
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
        return true;
    }
}
