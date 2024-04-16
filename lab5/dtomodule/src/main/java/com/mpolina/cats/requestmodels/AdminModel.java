package com.mpolina.cats.requestmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminModel(
        @NotBlank
        @Size(min = 3, max = 50, message = "Username length must be >= 3 and <= 50")
        String username,

        @NotBlank
        @Size(min = 3, max = 50, message = "Password length must be >= 3 and <= 50")
        String password) {
}

