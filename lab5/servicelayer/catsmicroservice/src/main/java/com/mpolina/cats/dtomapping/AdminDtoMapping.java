package com.mpolina.cats.dtomapping;

import com.mpolina.cats.getdtos.AdminDto;
import com.mpolina.cats.entity.user.User;

public class AdminDtoMapping {
    public static AdminDto asDto(User user) {
        return new AdminDto(user.getId(), user.getUsername(), user.getRole().toString());
    }
}