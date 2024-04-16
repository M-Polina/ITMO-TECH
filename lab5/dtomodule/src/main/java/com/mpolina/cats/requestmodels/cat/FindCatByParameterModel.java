package com.mpolina.cats.requestmodels.cat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record FindCatByParameterModel (
        String name,

        Date birthday,

        String breed,

        String color,

        @NotBlank
        String username) {
}
