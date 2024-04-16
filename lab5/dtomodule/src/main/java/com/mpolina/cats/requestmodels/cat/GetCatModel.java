package com.mpolina.cats.requestmodels.cat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.util.Date;

public record GetCatModel (
        @Min(1)
        Long catId,

        @NotBlank
        String username) {
}
