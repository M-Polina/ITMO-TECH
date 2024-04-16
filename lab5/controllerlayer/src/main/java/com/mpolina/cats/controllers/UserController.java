package com.mpolina.cats.controllers;

import com.mpolina.cats.entity.user.UserRole;
import com.mpolina.cats.kafka.KafkaSender;
import com.mpolina.cats.requestmodels.user.AdminModel;
import com.mpolina.cats.requestmodels.user.GetUserModel;
import com.mpolina.cats.requestmodels.user.UpdateUserModel;
import com.mpolina.cats.requestmodels.user.UserModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final KafkaSender kafkaSender;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<String> getRoles() {
        return kafkaSender.sendGetUserRoles();
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createAdmin(@Valid @RequestBody AdminModel adminModel) {
        AdminModel model = new AdminModel(adminModel.username(), adminModel.password());
        kafkaSender.sendCreateAdmin(model);
    }

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createUser(@Valid @RequestBody UserModel userModel) {
        UserModel model = new UserModel(userModel.username(), userModel.password(), userModel.ownerId());
        kafkaSender.sendCreateUser(model);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getUserById(@Min(1) @PathVariable Long id,
                              @AuthenticationPrincipal(expression = "username") String username) {
        GetUserModel model = new GetUserModel(id, username);
        return kafkaSender.sendGetUserById(model);
    }

    @PutMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateUserInformation(@Valid @RequestBody AdminModel adminModel,
                                      @NotBlank
                                      @Size(min = 3, max = 50, message = "Username length must be >= 3 and <= 50")
                                      @PathVariable String username) {
        UpdateUserModel model = new UpdateUserModel(username, adminModel.username(), adminModel.password(), UserRole.USER.toString());
        kafkaSender.sendUpdateUser(model);
    }

    @PutMapping("/admin/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateAdminInformation(@Valid @RequestBody AdminModel adminModel,
                                       @NotBlank
                                       @Size(min = 3, max = 50, message = "Username length must be >= 3 and <= 50")
                                       @PathVariable String username) {
        UpdateUserModel model = new UpdateUserModel(username, adminModel.username(), adminModel.password(), UserRole.ADMIN.toString());
        kafkaSender.sendUpdateUser(model);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(@Min(1) @PathVariable Long id) {
        kafkaSender.sendDeleteUser(id);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<String> findAllUsers(@AuthenticationPrincipal(expression = "username") String username) {
        return kafkaSender.sendFindAllUsers(username);
    }
}

