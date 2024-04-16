package com.mpolina.cats.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserModel(
        @NotBlank
        @Size(min = 3, max = 50, message = "Username length must be >= 3 and <= 50")
        String username,

        @NotBlank
        @Size(min = 3, max = 50, message = "Password length must be >= 3 and <= 50")
        String password,

        @Min(1)
        Long ownerId) {
}
