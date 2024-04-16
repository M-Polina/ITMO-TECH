package com.mpolina.cats.dtomapping;


import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.owner.Owner;

import java.util.ArrayList;
import java.util.List;

public class OwnerDtoMapping {
    public static OwnerDto asDto(Owner owner) {
        List<Long> catsIdList = new ArrayList<>();
        for (Cat cat : owner.getCatsList()) {
            catsIdList.add(cat.getId());
        }

        return new OwnerDto(owner.getId(), owner.getName(), owner.getSurname(), owner.getBirthday(), catsIdList);
    }
}