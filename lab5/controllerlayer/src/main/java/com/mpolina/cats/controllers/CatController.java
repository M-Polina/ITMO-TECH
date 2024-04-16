package com.mpolina.cats.controllers;

import com.mpolina.cats.kafka.KafkaSender;
import com.mpolina.cats.requestmodels.cat.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cats")
@RequiredArgsConstructor
public class CatController {
    private final KafkaSender kafkaSender;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createCat(@Valid @RequestBody CatModel catModel) {
        kafkaSender.sendCreateCat(catModel);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getCatById(@Min(1) @PathVariable Long id,
                             @AuthenticationPrincipal(expression = "username") String username) {
        GetCatModel model = new GetCatModel(id, username);
        return kafkaSender.sendGetCatById(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateCatInformation(@Valid @RequestBody CatModel catModel, @Min(1) @PathVariable Long id) {
        UpdateCatModel model = new UpdateCatModel(id, catModel);
        kafkaSender.sendUpdateCatInformation(model);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteCat(@Min(1) @PathVariable Long id) {
        kafkaSender.sendDeleteCat(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String findAllCats(@AuthenticationPrincipal(expression = "username") String username) {
        return kafkaSender.sendFindAllCats(username);
    }

    @GetMapping("/name")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String findCatsByName(@NotBlank @RequestParam String name,
                                 @AuthenticationPrincipal(expression = "username") String username) {
        FindCatByParameterModel model = new FindCatByParameterModel(name, null, null, null, username);
        return kafkaSender.sendFindCatsByName(model);
    }

    @GetMapping("/birthday")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String findCatsByBirthday(@Past @RequestParam Date birthday,
                                     @AuthenticationPrincipal(expression = "username") String username) {
        FindCatByParameterModel model = new FindCatByParameterModel(null, birthday, null, null, username);
        return kafkaSender.sendFindCatsByBirthday(model);
    }

    @GetMapping("/breed")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String findCatsByBreed(@NotBlank String breed,
                                  @AuthenticationPrincipal(expression = "username") String username) {
        FindCatByParameterModel model = new FindCatByParameterModel(null, null, breed, null, username);
        return kafkaSender.sendFindCatsByBreed(model);
    }

    @GetMapping("/color")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String findCatsByColor(@NotBlank String color,
                                  @AuthenticationPrincipal(expression = "username") String username) {
        FindCatByParameterModel model = new FindCatByParameterModel(null, null, null, color, username);
        return kafkaSender.sendFindCatsByColor(model);
    }

    @GetMapping("/colors")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    List<String> colors() {
        return kafkaSender.sendGetCatColors();
    }

    @GetMapping("/friendship/check")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    boolean checkCatsFriendshipPresence(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        FriendCatsModel model = new FriendCatsModel(catId1, catId2);
        return kafkaSender.sendCheckCatsFriendshipPresence(model);
    }

    @PatchMapping("/friendship/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    void makeCatsFriends(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        FriendCatsModel model = new FriendCatsModel(catId1, catId2);
        kafkaSender.sendMakeCatsFriends(model);
    }

    @PatchMapping("/friendship/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    void deleteCatsFriendship(@Min(1) @RequestParam Long catId1, @Min(1) @RequestParam Long catId2) {
        FriendCatsModel model = new FriendCatsModel(catId1, catId2);
        kafkaSender.sendBreakeCatsFriendship(model);
    }
}
