package com.mpolina.cats.requestmodels.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GetUserModel(
        @Min(1)
        Long userId,

        @NotBlank
        String username) {
}
