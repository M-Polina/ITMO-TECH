package com.mpolina.cats.services;

import com.mpolina.cats.dto.AdminDto;
import com.mpolina.cats.dto.UserDto;

import java.util.List;

public interface UserService {
    List<String> getUserRoles();

    AdminDto createAdmin(String username, String password);

    UserDto createUser(String username, String password, Long ownerId);

    UserDto updateUser(String username, String newUsername, String password, String role);

    void deleteUser(Long userId);

    List<UserDto> findAllUsers(String username);

    UserDto getUserById(Long userId, String username);
}
