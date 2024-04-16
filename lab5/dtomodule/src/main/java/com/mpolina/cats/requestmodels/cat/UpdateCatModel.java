package com.mpolina.cats.requestmodels.cat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record UpdateCatModel (
        @Min(1)
        Long catId,

        @NotNull
        CatModel catModel){
}
