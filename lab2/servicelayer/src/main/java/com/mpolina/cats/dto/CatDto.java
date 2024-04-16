package com.mpolina.cats.dto;

import com.mpolina.cats.entity.cat.CatColor;
import com.mpolina.cats.entity.owner.Owner;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

public record CatDto(Long catId, String name, Date birthday, String breed, String color, Long ownerId, List<Long> catFriendsIdList) {
}
