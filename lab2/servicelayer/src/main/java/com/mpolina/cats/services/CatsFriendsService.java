package com.mpolina.cats.services;

import com.mpolina.cats.dao.CatDao;
import com.mpolina.cats.dao.OwnerDao;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.exceptions.CatsLabServiceException;
import lombok.Getter;

public class CatsFriendsService {
    @Getter
    private CatDao catDao;

    @Getter
    private OwnerDao ownerDao;

    public CatsFriendsService(CatDao catDao, OwnerDao ownerDao) {
        if (catDao == null || ownerDao == null) {
            throw new CatsLabServiceException("Null dao while creating cat service.");
        }

        this.catDao = catDao;
        this.ownerDao = ownerDao;
    }

    public boolean checkCatsFriendshipPresence(Long catId1, Long catId2){
        if (catId1 == null || catId2 == null){
            return false;
        }

        Cat foundCat1 = catDao.findById(catId1);
        if(foundCat1 == null){
            return false;
        }

        Cat foundCat2 = catDao.findById(catId2);
        if(foundCat2 == null){
            return false;
        }

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId2);
        boolean cat2HasCat1AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId1);

        if(cat1HasCat2AsFriend && cat2HasCat1AsFriend){
            return true;
        }

        return false;
    }

    public void makeCatsFriends(Long catId1, Long catId2){
        if (catId1 == null || catId2 == null){
            throw new CatsLabServiceException("Null id while making cats friends.");
        }

        Cat foundCat1 = catDao.findById(catId1);
        if(foundCat1 == null){
            throw new CatsLabServiceException("No cat with this first Id while making cats friends.");
        }

        Cat foundCat2 = catDao.findById(catId2);
        if(foundCat2 == null){
            throw new CatsLabServiceException("No cat with this second Id while making cats friends.");
        }

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId2);
        boolean cat2HasCat1AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId1);

        if(cat1HasCat2AsFriend && cat2HasCat1AsFriend){
            throw new CatsLabServiceException("Cats already are friends while making cats friends.");
        }

        if(cat1HasCat2AsFriend || cat2HasCat1AsFriend){
            throw new CatsLabServiceException("Cats are not mutual friends while making cats friends.");
        }

        foundCat1.getFriendCatsList().add(foundCat2);
        foundCat2.getFriendCatsList().add(foundCat1);

        catDao.update(foundCat1);
        catDao.update(foundCat2);
    }

    public void breakeCatsFriendship(Long catId1, Long catId2){
        if (catId1 == null || catId2 == null){
            throw new CatsLabServiceException("Null id while braking cats friendship.");
        }

        Cat foundCat1 = catDao.findById(catId1);
        if(foundCat1 == null){
            throw new CatsLabServiceException("No cat with this first Id while braking cats friendship.");
        }

        Cat foundCat2 = catDao.findById(catId2);
        if(foundCat2 == null){
            throw new CatsLabServiceException("No cat with this second Id while braking cats friendship.");
        }

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId2);
        boolean cat2HasCat1AsFriend = foundCat2.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId1);

        if((!cat1HasCat2AsFriend) || (!cat2HasCat1AsFriend)){
            throw new CatsLabServiceException("Cats are not friends  while braking cats friendship.");
        }

        if(!cat1HasCat2AsFriend){
            throw new CatsLabServiceException("First cat hasn't got second as friend  while braking cats friendship.");
        }

        if(!cat2HasCat1AsFriend){
            throw new CatsLabServiceException("Second cat hasn't got first as friend while braking cats friendship.");
        }

        foundCat1.getFriendCatsList().removeIf(c -> (c.getId() == foundCat2.getId()));
        foundCat2.getFriendCatsList().removeIf(c -> (c.getId() == foundCat1.getId()));

        catDao.update(foundCat1);
        catDao.update(foundCat2);
    }
}
