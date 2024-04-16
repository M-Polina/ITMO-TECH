package com.mpolina.cats.requestmodels.owner;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GetOwnerModel(
        @Min(1)
        Long ownerId,

        @NotBlank
        String username) {
}
