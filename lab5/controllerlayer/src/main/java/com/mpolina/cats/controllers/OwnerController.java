package com.mpolina.cats.controllers;

import com.mpolina.cats.kafka.KafkaSender;
import com.mpolina.cats.requestmodels.owner.GetOwnerModel;
import com.mpolina.cats.requestmodels.owner.OwnerModel;
import com.mpolina.cats.requestmodels.owner.UpdateOwnerModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final KafkaSender kafkaSender;


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createOwner(@Valid @RequestBody OwnerModel ownerModel) {
        kafkaSender.sendCreateOwner(ownerModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getOwnerById(@Min(1) @PathVariable Long id,
                               @AuthenticationPrincipal(expression = "username") String username) {

        GetOwnerModel model = new GetOwnerModel(id, username);
        return kafkaSender.sendGetOwnerById(model);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateOwnerInformation(@Valid @RequestBody OwnerModel ownerModel, @Min(1) @PathVariable Long id) {
        UpdateOwnerModel model = new UpdateOwnerModel(id, ownerModel);
        kafkaSender.sendUpdateOwnerInformation(model);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteOwner(@Min(1) @PathVariable Long id) {
        kafkaSender.sendDeleteOwner(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String findAllOwners(@AuthenticationPrincipal(expression = "username") String username) {
        return kafkaSender.sendFindAllOwners(username);
    }

    @GetMapping("/name")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String findOwnersByName(@NotBlank @RequestParam String name) {
        return kafkaSender.sendFindOwnersByName(name);
    }

    @GetMapping("/surname")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String findOwnersBySurname(@NotBlank @RequestParam String surname) {
        return kafkaSender.sendFindOwnersBySurname(surname);
    }

    @GetMapping("/birthday")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String findOwnersByBirthday(@Past @RequestParam Date birthday) {
        return kafkaSender.sendFindOwnersByBirthday(birthday);
    }
}