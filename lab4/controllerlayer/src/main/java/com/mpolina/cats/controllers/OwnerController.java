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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public OwnerDto createOwner(@Valid @RequestBody OwnerModel ownerModel) {
        return ownerService.createOwner(ownerModel.name(), ownerModel.surname(), ownerModel.birthday());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    OwnerDto getOwnerById(@Min(1) @PathVariable Long id,
                          @AuthenticationPrincipal(expression = "username") String username) {

        return ownerService.getOwnerById(id, username);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    OwnerDto updateOwnerInformation(@Valid @RequestBody OwnerModel ownerModel, @Min(1) @PathVariable Long id) {
        return ownerService.updateOwnerInformation(id, ownerModel.name(), ownerModel.surname(), ownerModel.birthday());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    void deleteOwner(@Min(1) @PathVariable Long id) {
        ownerService.deleteOwner(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<OwnerDto> findAllOwners(@AuthenticationPrincipal(expression = "username") String username) {
        return ownerService.findAllOwners(username);
    }

    @GetMapping("/name")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<OwnerDto> findOwnersByName(@NotBlank @RequestParam String name) {
        return ownerService.findOwnersByName(name);
    }

    @GetMapping("/surname")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<OwnerDto> findOwnersBySurname(@NotBlank @RequestParam String surname) {
        return ownerService.findOwnersBySurname(surname);
    }

    @GetMapping("/birthday")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<OwnerDto> findOwnersByBirthday(@Past @RequestParam Date birthday) {
        return ownerService.findOwnersByBirthday(birthday);
    }

    @GetMapping("/{id}/cats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<CatDto> findOwnersCats(@Min(1) @PathVariable Long id,
                                  @AuthenticationPrincipal(expression = "username") String username) {
        return ownerService.getCats(id, username);
    }
}