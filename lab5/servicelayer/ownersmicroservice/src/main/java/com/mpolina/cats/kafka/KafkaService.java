package com.mpolina.cats.kafka;

import com.mpolina.cats.getdtos.OwnerDto;
import com.mpolina.cats.getdtos.UserDto;
import com.mpolina.cats.requestmodels.owner.GetOwnerModel;
import com.mpolina.cats.requestmodels.owner.OwnerModel;
import com.mpolina.cats.requestmodels.user.AdminModel;
import com.mpolina.cats.requestmodels.user.GetUserModel;
import com.mpolina.cats.requestmodels.user.UpdateUserModel;
import com.mpolina.cats.requestmodels.user.UserModel;
import com.mpolina.cats.service.OwnerService;
import com.mpolina.cats.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class KafkaService {
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private UserService userService;


    @KafkaListener(topics = "create_owner", groupId = "endpoints")
    public void sendCreateOwner(OwnerModel msg) {
        ownerService.createOwner(msg);
    }

    @KafkaListener(topics = "delete_owner", groupId = "endpoints")
    public void sendCreateOwner(Long msg) {
        ownerService.deleteOwner(msg);
    }

    @KafkaListener(topics = "get_owner", groupId = "endpoints")
    public OwnerDto sendGetOwnerById(GetOwnerModel msg) {
        return ownerService.getOwnerById(msg);
    }

    @KafkaListener(topics = "find_all_owners", groupId = "endpoints")
    public List<OwnerDto> sendFindAllOwners(String msg) {
        return ownerService.findAllOwners(msg);
    }

    @KafkaListener(topics = "find_all_owners_by_name", groupId = "endpoints")
    public List<OwnerDto> sendFindOwnersByName(String msg) {
        return ownerService.findOwnersByName(msg);
    }

    @KafkaListener(topics = "find_all_owners_by_surename", groupId = "endpoints")
    public List<OwnerDto> sendFindOwnersBySurname(String msg) {
        return ownerService.findOwnersBySurname(msg);
    }

    @KafkaListener(topics = "find_all_owners_by_birthday", groupId = "endpoints")
    public List<OwnerDto> sendFindOwnersByBirthday(Date msg) {
        return ownerService.findOwnersByBirthday(msg);
    }

    @KafkaListener(topics = "get_roles", groupId = "endpoints")
    public List<String> sendGetUserRoles() {
        return userService.getUserRoles();
    }

    @KafkaListener(topics = "create_admin", groupId = "endpoints")
    public void sendCreateAdmin(AdminModel msg) {
        userService.createAdmin(msg);
    }

    @KafkaListener(topics = "create_user", groupId = "endpoints")
    public void sendCreateUser(UserModel msg) {
        userService.createUser(msg);
    }

    @KafkaListener(topics = "get_user", groupId = "endpoints")
    public UserDto sendGetUserById(GetUserModel msg) {
        return userService.getUserById(msg);
    }

    @KafkaListener(topics = "update_user", groupId = "endpoints")
    public void sendUpdateUser(UpdateUserModel msg) {
        userService.updateUser(msg);
    }

    @KafkaListener(topics = "delete_user", groupId = "endpoints")
    public void sendDeleteUser(Long msg) {
        userService.deleteUser(msg);
    }

    @KafkaListener(topics = "find_users", groupId = "endpoints")
    public List<UserDto> sendFindAllUsers(String msg) {
        return userService.findAllUsers(msg);
    }
}
