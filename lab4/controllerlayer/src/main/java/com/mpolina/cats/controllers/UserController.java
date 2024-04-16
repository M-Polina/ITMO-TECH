package com.mpolina.cats.controllers;

import com.mpolina.cats.dto.AdminDto;
import com.mpolina.cats.dto.UserDto;
import com.mpolina.cats.entity.user.UserRole;
import com.mpolina.cats.models.AdminModel;
import com.mpolina.cats.models.UserModel;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.OwnerService;
import com.mpolina.cats.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final OwnerService ownerService;
    private final CatService catService;
    private final UserService userService;

    @Autowired
    public UserController(
            OwnerService ownerService,
            CatService catService,
            UserService userService
    ) {
        this.ownerService = ownerService;
        this.catService = catService;
        this.userService = userService;
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<String> getRoles() {
        return userService.getUserRoles();
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AdminDto createAdmin(@Valid @RequestBody AdminModel adminModel) {
        return userService.createAdmin(adminModel.username(), adminModel.password());
    }

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDto createUser(@Valid @RequestBody UserModel userModel) {
        return userService.createUser(userModel.username(), userModel.password(), userModel.ownerId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public UserDto getUserById(@Min(1) @PathVariable Long id,
                               @AuthenticationPrincipal(expression = "username") String username) {
        return userService.getUserById(id, username);
    }

    @PutMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDto updateUserInformation(@Valid @RequestBody AdminModel adminModel,
                                         @NotBlank
                                         @Size(min = 3, max = 50, message = "Username length must be >= 3 and <= 50")
                                         @PathVariable String username) {
        return userService.updateUser(username, adminModel.username(), adminModel.password(), UserRole.USER.toString());
    }

    @PutMapping("/admin/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDto updateAdminInformation(@Valid @RequestBody AdminModel adminModel,
                                         @NotBlank
                                         @Size(min = 3, max = 50, message = "Username length must be >= 3 and <= 50")
                                         @PathVariable String username) {
        return userService.updateUser(username, adminModel.username(), adminModel.password(), UserRole.ADMIN.toString());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(@Min(1) @PathVariable Long id) {
        userService.deleteUser(id);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<UserDto> findAllUsers(@AuthenticationPrincipal(expression = "username") String username) {
        return userService.findAllUsers(username);
    }
}

