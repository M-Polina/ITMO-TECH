package com.mpolina.cats.service;

import com.mpolina.cats.getdtos.CatDto;
import com.mpolina.cats.getdtos.OwnerDto;
import com.mpolina.cats.requestmodels.cat.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CatService {
    void createCat(CatModel catModel);

    CatDto getCatById(GetCatModel getCatModel);

    void updateCatInformation(UpdateCatModel updateCatModel);

    void deleteCat(Long catId);

    List<CatDto> findAllCats(String username);

    List<CatDto> findCatsByName(FindCatByParameterModel findCatByParameterModel);

    List<CatDto> findCatsByBirthday(FindCatByParameterModel findCatByParameterModel);

    List<CatDto> findCatsByColor(FindCatByParameterModel findCatByParameterModel);

    List<CatDto> findCatsByBreed(FindCatByParameterModel findCatByParameterModel);

    OwnerDto getCatOwner(GetCatModel getCatModel);

    List<String> getCatColors();

    boolean checkCatsFriendshipPresence(FriendCatsModel friendCatsModel);
    void makeCatsFriends(FriendCatsModel friendCatsModel);
    void breakeCatsFriendship(FriendCatsModel friendCatsModel);
}
