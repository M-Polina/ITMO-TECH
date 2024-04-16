package com.mpolina.cats.requestmodels.owner;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record UpdateOwnerModel (
        @Min(1)
        Long ownerId,

        @NotNull
        OwnerModel ownerModel) {
}
