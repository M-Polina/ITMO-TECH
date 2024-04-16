package com.mpolina.cats.requestmodels.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserModel(
        @NotBlank
        String username,

        @NotBlank
        String newUsername,

        @NotBlank
        String password,

        @NotBlank
        String role) {
}