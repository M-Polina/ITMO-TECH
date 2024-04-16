package com.mpolina.cats.requestmodels;


import jakarta.validation.constraints.*;
import java.util.Date;

public record CatModel(
        @NotBlank
        String name,

        @Past
        Date birthday,

        @NotBlank
        String breed,

        @NotBlank
        String color,

        @Min(1)
        Long ownerId) {
}

