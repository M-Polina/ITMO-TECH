package com.mpolina.cats.kafka;

import com.mpolina.cats.requestmodels.cat.*;
import com.mpolina.cats.requestmodels.owner.GetOwnerModel;
import com.mpolina.cats.requestmodels.owner.OwnerModel;
import com.mpolina.cats.requestmodels.owner.UpdateOwnerModel;
import com.mpolina.cats.requestmodels.user.AdminModel;
import com.mpolina.cats.requestmodels.user.GetUserModel;
import com.mpolina.cats.requestmodels.user.UpdateUserModel;
import com.mpolina.cats.requestmodels.user.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {
    @Autowired
    private final KafkaTemplate<String, Long> idKafkaTemplate;
    private final KafkaTemplate<String, OwnerModel> createOwnerKafkaTemplate;
    private final KafkaTemplate<String, UpdateOwnerModel> updateOwnerKafkaTemplate;
    private final KafkaTemplate<String, CatModel> createCatKafkaTemplate;
    private final KafkaTemplate<String, UpdateCatModel> updateCatKafkaTemplate;
    private final KafkaTemplate<String, GetCatModel> getCatKafkaTemplate;
    private final KafkaTemplate<String, String> findAllCatsKafkaTemplate;
    private final KafkaTemplate<String, FindCatByParameterModel> findCatsByKafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, FriendCatsModel> friendsTemplate;
    private final KafkaTemplate<String, GetOwnerModel> getOwnerKafkaTemplate;
    private final KafkaTemplate<String, String> findAllOwnersKafkaTemplate;
    private final KafkaTemplate<String, Date> ownersByDateKafkaTemplate;
    private final KafkaTemplate<String, AdminModel> createAdminKafkaTemplate;
    private final KafkaTemplate<String, UserModel> createUserKafkaTemplate;
    private final KafkaTemplate<String, GetUserModel> getUserKafkaTemplate;
    private final KafkaTemplate<String, UpdateUserModel> updateUserKafkaTemplate;


    public String sendGetCatById(GetCatModel message) {
        CompletableFuture<SendResult<String, GetCatModel>> future = getCatKafkaTemplate.send("get_cat", message);
        try {
           return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCreateOwner(OwnerModel msg) {
        createOwnerKafkaTemplate.send("create_owner", msg);
    }

    public void sendUpdateOwnerInformation(UpdateOwnerModel msg) {
        updateOwnerKafkaTemplate.send("update_owner", msg);
    }


    public void sendDeleteOwner(Long msg) {
        idKafkaTemplate.send("delete_owner", msg);
    }

    public void sendCreateCat(CatModel msg) {
        createCatKafkaTemplate.send("create_cat", msg);
    }

    public void sendUpdateCatInformation(UpdateCatModel msg) {
        updateCatKafkaTemplate.send("update_cat", msg);
    }


    public void sendDeleteCat(Long msg) {
        idKafkaTemplate.send("delete_cat", msg);
    }
    public String sendFindAllCats(String msg) {
        CompletableFuture<SendResult<String, String>> future = findAllCatsKafkaTemplate.send("find_all_cats", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindCatsByName(FindCatByParameterModel msg) {
        CompletableFuture<SendResult<String, FindCatByParameterModel>> future = findCatsByKafkaTemplate.send("find_cats_by_name", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindCatsByColor(FindCatByParameterModel msg) {
        CompletableFuture<SendResult<String, FindCatByParameterModel>> future = findCatsByKafkaTemplate.send("find_cats_by_colore", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindCatsByBreed(FindCatByParameterModel msg) {
        CompletableFuture<SendResult<String, FindCatByParameterModel>> future = findCatsByKafkaTemplate.send("find_cats_by_breed", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindCatsByBirthday(FindCatByParameterModel msg) {
        CompletableFuture<SendResult<String, FindCatByParameterModel>> future = findCatsByKafkaTemplate.send("find_cat_by_birthday", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public List<String> sendGetCatColors() {
        CompletableFuture<SendResult<String, String>> future = stringKafkaTemplate.send("get_colors", "colors");
        try {
            return List.of(future.get().getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean sendCheckCatsFriendshipPresence(FriendCatsModel msg) {
        CompletableFuture<SendResult<String, FriendCatsModel>> future = friendsTemplate.send("check_friendship", msg);
        return true;
    }

    public void sendMakeCatsFriends(FriendCatsModel msg) {
        friendsTemplate.send("create_friendship", msg);
    }

    public void sendBreakeCatsFriendship(FriendCatsModel msg) {
        friendsTemplate.send("delete_friendship", msg);
    }

    public String sendGetOwnerById(GetOwnerModel msg) {
        CompletableFuture<SendResult<String, GetOwnerModel>> future = getOwnerKafkaTemplate.send("get_owner", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindAllOwners(String msg) {
        CompletableFuture<SendResult<String, String>> future = findAllOwnersKafkaTemplate.send("find_all_owners", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindOwnersByName(String msg) {
        CompletableFuture<SendResult<String, String>> future = findAllOwnersKafkaTemplate.send("find_all_owners_by_name", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindOwnersBySurname(String msg) {
        CompletableFuture<SendResult<String, String>> future = findAllOwnersKafkaTemplate.send("find_all_owners_by_surename", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendFindOwnersByBirthday(Date msg) {
        CompletableFuture<SendResult<String, Date>> future = ownersByDateKafkaTemplate.send("find_all_owners_by_birthday", msg);
        try {
            return future.get().getProducerRecord().value().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> sendGetUserRoles() {
        CompletableFuture<SendResult<String, String>> future = stringKafkaTemplate.send("get_roles", "roles");
        try {
            return List.of(future.get().getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCreateAdmin(AdminModel msg) {
        createAdminKafkaTemplate.send("create_admin", msg);
    }

    public void sendCreateUser(UserModel msg) {
        createUserKafkaTemplate.send("create_user", msg);
    }


    public String sendGetUserById(GetUserModel msg) {
        CompletableFuture<SendResult<String, GetUserModel>> future = getUserKafkaTemplate.send("get_user", msg);
        try {
            return (future.get().getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUpdateUser(UpdateUserModel msg) {
        updateUserKafkaTemplate.send("update_user", msg);
    }

    public void sendDeleteUser(Long msg) {
        idKafkaTemplate.send("delete_user", msg);
    }

    public List<String> sendFindAllUsers(String msg) {
        CompletableFuture<SendResult<String, String>> future = stringKafkaTemplate.send("find_users", msg);
        try {
            return List.of(future.get().getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
