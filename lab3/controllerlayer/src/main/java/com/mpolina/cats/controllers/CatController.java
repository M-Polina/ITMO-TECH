package com.mpolina.cats.controllers;

import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.models.CatModel;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.CatsFriendsService;
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
@RequestMapping("/cats")
public class CatController {
    private final OwnerService ownerService;
    private final CatService catService;
    private final CatsFriendsService catsFriendsService;

    @Autowired
    public CatController(
            OwnerService ownerService,
            CatService catService,
            CatsFriendsService catsFriendsService
    ) {
        this.ownerService = ownerService;
        this.catService = catService;
        this.catsFriendsService = catsFriendsService;
    }

    @PostMapping
    public CatDto createCat(@Valid @RequestBody CatModel catModel) {
        return catService.createCat(catModel.name(), catModel.birthday(), catModel.breed(), catModel.color(), catModel.ownerId());
    }

    @GetMapping("/{id}")
    CatDto getCatById(@Min(1) @PathVariable Long id) {
        return catService.getCatById(id);
    }

    @PutMapping("/{id}")
    CatDto updateCatInformation(@Valid @RequestBody CatModel catModel, @Min(1) @PathVariable Long id) {
        return catService.updateCatInformation(id, catModel.name(), catModel.birthday(), catModel.breed(), catModel.color(), catModel.ownerId());
    }

    @DeleteMapping("/{id}")
    void deleteCat(@Min(1) @PathVariable Long id) {
        catService.deleteCat(id);
    }

    @GetMapping
    List<CatDto> findAllCats() {
        return catService.findAllCats();
    }

    @GetMapping("/name")
    List<CatDto> findCatsByName(@NotBlank @RequestParam String name) {
        return catService.findCatsByName(name);
    }

    @GetMapping("/birthday")
    List<CatDto> findCatsByBirthday(@Past @RequestParam Date birthday) {
        return catService.findCatsByBirthday(birthday);
    }

    @GetMapping("/breed")
    List<CatDto> findCatsByBreed(@NotBlank String breed) {
        return catService.findCatsByBreed(breed);
    }

    @GetMapping("/color")
    List<CatDto> findCatsByColor(@NotBlank String color) {
        return catService.findCatsByColor(color);
    }

    @GetMapping("/{id}/owner")
    OwnerDto getOwner(@Min(1) @PathVariable Long id) {
        return catService.getCatOwner(id);
    }

    @GetMapping("/colors")
    List<String> colors() {
        return catService.getCatColors();
    }

    @GetMapping("/friendship/check")
    boolean checkCatsFriendshipPresence(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        return catsFriendsService.checkCatsFriendshipPresence(catId1, catId2);
    }

    @PutMapping("/friendship/create")
    void makeCatsFriends(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        catsFriendsService.makeCatsFriends(catId1, catId2);
    }

    @PutMapping("/friendship/delete")
    void deleteCatsFriendship(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        catsFriendsService.breakeCatsFriendship(catId1, catId2);
    }
}
