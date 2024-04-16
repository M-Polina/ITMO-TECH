package com.mpolina.cats.requestmodels;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record OwnerModel(
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @Past
        Date birthday) {
}

