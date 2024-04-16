package com.mpolina.cats.controllers;

import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.models.OwnerModel;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.OwnerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;
    private final CatService catService;

    @Autowired
    public OwnerController(
            OwnerService ownerService,
            CatService catService
    ) {
        this.ownerService = ownerService;
        this.catService = catService;
    }

    @PostMapping
    public OwnerDto createOwner(@Valid @RequestBody OwnerModel ownerModel) {
        return ownerService.createOwner(ownerModel.name(), ownerModel.surname(), ownerModel.birthday());
    }

    @GetMapping("/{id}")
    OwnerDto getOwnerById(@Min(1) @PathVariable Long id) {

        return ownerService.getOwnerById(id);
    }


    @PutMapping("/{id}")
    OwnerDto updateOwnerInformation(@Valid @RequestBody OwnerModel ownerModel, @Min(1) @PathVariable Long id) {
        return ownerService.updateOwnerInformation(id, ownerModel.name(), ownerModel.surname(), ownerModel.birthday());
    }

    @DeleteMapping("/{id}")
    void deleteOwner(@Min(1) @PathVariable Long id) {
        ownerService.deleteOwner(id);
    }

    @GetMapping
    List<OwnerDto> findAllOwners() {
        return ownerService.findAllOwners();
    }

    @GetMapping("/name")
    List<OwnerDto> findOwnersByName(@NotBlank @RequestParam String name) {
        return ownerService.findOwnersByName(name);
    }

    @GetMapping("/surname")
    List<OwnerDto> findOwnersBySurname(@NotBlank @RequestParam String surname) {
        return ownerService.findOwnersBySurname(surname);
    }

    @GetMapping("/birthday")
    List<OwnerDto> findOwnersByBirthday(@Past @RequestParam Date birthday) {
        return ownerService.findOwnersByBirthday(birthday);
    }

    @GetMapping("/{id}/cats")
    List<CatDto> findOwnersByName(@Min(1) @PathVariable Long id) {
        return ownerService.getCats(id);
    }
}