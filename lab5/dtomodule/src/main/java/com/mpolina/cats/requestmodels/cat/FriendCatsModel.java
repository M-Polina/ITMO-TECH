package com.mpolina.cats.requestmodels.cat;

import jakarta.validation.constraints.Min;

public record FriendCatsModel(
        @Min(1)
        Long catId1,

        @Min(1)
        Long catId2
) {
}
