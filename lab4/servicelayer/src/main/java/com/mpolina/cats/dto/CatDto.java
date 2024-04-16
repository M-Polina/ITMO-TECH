package com.mpolina.cats.dto;

import java.util.Date;
import java.util.List;

public record CatDto(Long catId, String name, Date birthday, String breed, String color, Long ownerId, List<Long> catFriendsIdList) {
}
