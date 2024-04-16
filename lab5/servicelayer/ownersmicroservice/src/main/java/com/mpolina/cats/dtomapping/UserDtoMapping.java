package com.mpolina.cats.dtomapping;

import com.mpolina.cats.entity.user.User;
import com.mpolina.cats.getdtos.UserDto;

public class UserDtoMapping {
    public static UserDto asDto(User user) {
        if (user.getOwner() == null){
            return new UserDto(user.getId(), user.getUsername(), user.getRole().toString(), null);
        }
        return new UserDto(user.getId(), user.getUsername(), user.getRole().toString(), user.getOwner().getId());
    }
}