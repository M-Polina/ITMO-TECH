package com.mpolina.cats.dtomapping;

import com.mpolina.cats.getdtos.CatDto;
import com.mpolina.cats.entity.cat.Cat;

import java.util.ArrayList;
import java.util.List;

public class CatDtoMapping {
    public static CatDto asDto(Cat cat) {
        List<Long> catFriendsIdList = new ArrayList<>();
        if (cat.getFriendCatsList() != null) {
            for (Cat cati : cat.getFriendCatsList()) {
                catFriendsIdList.add(cati.getId());
            }
        }

        return new CatDto(cat.getId(), cat.getName(), cat.getBirthday(), cat.getBreed(), cat.getColor().toString(), cat.getOwner().getId(), catFriendsIdList);
    }
}
