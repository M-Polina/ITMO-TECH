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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CatDto createCat(@Valid @RequestBody CatModel catModel) {
        return catService.createCat(catModel.name(), catModel.birthday(), catModel.breed(), catModel.color(), catModel.ownerId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    CatDto getCatById(@Min(1) @PathVariable Long id,
                      @AuthenticationPrincipal(expression = "username") String username) {
        return catService.getCatById(id, username);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    CatDto updateCatInformation(@Valid @RequestBody CatModel catModel, @Min(1) @PathVariable Long id) {
        return catService.updateCatInformation(id, catModel.name(), catModel.birthday(), catModel.breed(), catModel.color(), catModel.ownerId());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    void deleteCat(@Min(1) @PathVariable Long id) {
        catService.deleteCat(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<CatDto> findAllCats(@AuthenticationPrincipal(expression = "username") String username) {
        return catService.findAllCats(username);
    }

    @GetMapping("/name")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<CatDto> findCatsByName(@NotBlank @RequestParam String name,
                                @AuthenticationPrincipal(expression = "username") String username) {
        return catService.findCatsByName(name, username);
    }

    @GetMapping("/birthday")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<CatDto> findCatsByBirthday(@Past @RequestParam Date birthday,
                                    @AuthenticationPrincipal(expression = "username") String username) {
        return catService.findCatsByBirthday(birthday, username);
    }

    @GetMapping("/breed")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<CatDto> findCatsByBreed(@NotBlank String breed,
                                 @AuthenticationPrincipal(expression = "username") String username) {
        return catService.findCatsByBreed(breed, username);
    }

    @GetMapping("/color")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<CatDto> findCatsByColor(@NotBlank String color,
                                 @AuthenticationPrincipal(expression = "username") String username) {
        return catService.findCatsByColor(color, username);
    }

    @GetMapping("/{id}/owner")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    OwnerDto getOwner(@Min(1) @PathVariable Long id,
                      @AuthenticationPrincipal(expression = "username") String username) {
        return catService.getCatOwner(id, username);
    }

    @GetMapping("/colors")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<String> colors() {
        return catService.getCatColors();
    }

    @GetMapping("/friendship/check")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    boolean checkCatsFriendshipPresence(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        return catsFriendsService.checkCatsFriendshipPresence(catId1, catId2);
    }

    @PatchMapping("/friendship/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    void makeCatsFriends(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        catsFriendsService.makeCatsFriends(catId1, catId2);
    }   

    @PatchMapping("/friendship/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    void deleteCatsFriendship(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        catsFriendsService.breakeCatsFriendship(catId1, catId2);
    }
}
