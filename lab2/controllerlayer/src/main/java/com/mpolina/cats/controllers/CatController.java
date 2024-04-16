package com.mpolina.cats.controllers;

import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.CatsFriendsService;

import java.sql.Date;
import java.util.List;

public class CatController {
    private CatService catService;

    private CatsFriendsService catsFriendsService;

    public CatController(CatService catService, CatsFriendsService catsFriendsService) {
        this.catService = catService;
        this.catsFriendsService = catsFriendsService;
    }

    public CatDto findCatById(Long catId){
        CatDto catDto = catService.findCatById(catId);
        return catDto;
    }

    public List<CatDto> getAllCats(){
        List<CatDto> catDtoList = catService.getAllCats();
        return catDtoList;
    }

    public CatDto createCat(String name, Date birthday, String breed, String strCatColor, Long ownerId) {
        CatDto catDto = catService.createCat(name, birthday, breed, strCatColor, ownerId);
        return catDto;
    }

    public List<String> getCatColors(){
        List<String> catColorsList = catService.getCatColors();
        return catColorsList;
    }

    public CatDto updateCatInformation(Long catId, String name, Date birthday, String breed, String strCatColor, Long ownerId) {
        CatDto catDto = catService.updateCatInformation(catId, name, birthday, breed, strCatColor, ownerId);
        return catDto;
    }

    public boolean deleteCat(Long catId){
        try {
            catService.deleteCat(catId);
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
        return true;
    }

    public boolean checkCatsFriendshipPresence(Long catId1, Long catId2){
        boolean frenshipExists = catsFriendsService.checkCatsFriendshipPresence(catId1, catId2);
        return frenshipExists;
    }

    public boolean makeCatsFriends(Long catId1, Long catId2){
        try {
            catsFriendsService.makeCatsFriends(catId1, catId2);
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
        return true;
    }

    public boolean breakeCatsFriendship(Long catId1, Long catId2){
        try {
            catsFriendsService.breakeCatsFriendship(catId1, catId2);
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
        return true;
    }
}
