package com.mpolina.cats.service;

import com.mpolina.cats.getdtos.AdminDto;
import com.mpolina.cats.getdtos.UserDto;
import com.mpolina.cats.requestmodels.user.AdminModel;
import com.mpolina.cats.requestmodels.user.GetUserModel;
import com.mpolina.cats.requestmodels.user.UpdateUserModel;
import com.mpolina.cats.requestmodels.user.UserModel;

import java.util.List;

public interface UserService {
    List<String> getUserRoles();

    AdminDto createAdmin(AdminModel adminModel);

    UserDto createUser(UserModel userModel);

    UserDto updateUser(UpdateUserModel updateUserModel);

    void deleteUser(Long userId);

    List<UserDto> findAllUsers(String username);

    UserDto getUserById(GetUserModel getUserModel);
}
