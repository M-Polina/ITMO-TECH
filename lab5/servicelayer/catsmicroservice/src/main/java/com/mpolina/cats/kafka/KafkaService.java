package com.mpolina.cats.kafka;

import com.mpolina.cats.requestmodels.cat.*;
import com.mpolina.cats.service.CatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
//@Component
//@KafkaListener(id = "endpoints", topics = "endpointsTopic")
public class KafkaService {
    @Autowired
    private CatService catService;

    @KafkaListener(topics = "get_cat", groupId = "endpoints")
    public void sendGetCatById(GetCatModel message) {
        catService.getCatById(message);
    }

    @KafkaListener(topics = "create_cat", groupId = "endpoints")
    public void sendCreateCat(CatModel msg) {
        catService.createCat(msg);
    }

    @KafkaListener(topics = "update_cat", groupId = "endpoints")
    public void sendUpdateCatInformation(UpdateCatModel msg) {
        catService.updateCatInformation(msg);
    }

    @KafkaListener(topics = "delete_cat", groupId = "endpoints")
    public void sendDeleteCat(Long msg) {
        catService.deleteCat(msg);
    }

    @KafkaListener(topics = "find_all_cats", groupId = "endpoints")
    public void sendFindAllCats(String msg) {
        catService.findAllCats(msg);
    }

    @KafkaListener(topics = "find_cats_by_name", groupId = "endpoints")
    public void sendFindCatsByName(FindCatByParameterModel msg) {
        catService.findCatsByName(msg);
    }

    @KafkaListener(topics = "find_cats_by_colore", groupId = "endpoints")
    public void sendFindCatsByColor(FindCatByParameterModel msg) {
        catService.findCatsByColor(msg);
    }

    @KafkaListener(topics = "deletfind_cats_by_breede_cat", groupId = "endpoints")
    public void sendFindCatsByBreed(FindCatByParameterModel msg) {
        catService.findCatsByBreed(msg);
    }

    @KafkaListener(topics = "find_cat_by_birthday", groupId = "endpoints")
    public void sendFindCatsByBirthday(FindCatByParameterModel msg) {
        catService.findCatsByBirthday(msg);
    }


    @KafkaListener(topics = "get_colors", groupId = "endpoints")
    public void sendGetCatColors(String str) {
        catService.getCatColors();
    }

    @KafkaListener(topics = "check_friendship", groupId = "endpoints")
    public void sendCheckCatsFriendshipPresence(FriendCatsModel msg) {
        catService.checkCatsFriendshipPresence(msg);
    }

    @KafkaListener(topics = "create_friendship", groupId = "endpoints")
    public void sendMakeCatsFriends(FriendCatsModel msg) {
        catService.makeCatsFriends(msg);
    }

    @KafkaListener(topics = "delete_friendship", groupId = "endpoints")
    public void sendBreakeCatsFriendship(FriendCatsModel msg) {
        catService.breakeCatsFriendship(msg);
    }

}
