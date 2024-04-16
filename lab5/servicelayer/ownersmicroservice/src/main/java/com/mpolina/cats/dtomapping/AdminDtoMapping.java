package com.mpolina.cats.dtomapping;

import com.mpolina.cats.entity.user.User;
import com.mpolina.cats.getdtos.AdminDto;

public class AdminDtoMapping {
    public static AdminDto asDto(User user) {
        return new AdminDto(user.getId(), user.getUsername(), user.getRole().toString());
    }
}