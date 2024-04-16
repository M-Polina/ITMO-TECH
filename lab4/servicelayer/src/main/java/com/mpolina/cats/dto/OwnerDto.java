package com.mpolina.cats.dto;


import java.util.Date;
import java.util.List;

public record OwnerDto(Long ownerId, Long userId, String name, String surname, Date birthday, List<Long> catsIdList) {
}
