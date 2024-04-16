package com.mpolina.cats.dto;

import lombok.Getter;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

public record OwnerDto(Long ownerId, String name, String surname, Date birthday, List<Long> catsIdList) {
}
