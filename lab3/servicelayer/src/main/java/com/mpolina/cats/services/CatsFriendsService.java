package com.mpolina.cats.services;

import org.springframework.stereotype.Service;

@Service
public interface CatsFriendsService {
    boolean checkCatsFriendshipPresence(Long catId1, Long catId2);
    void makeCatsFriends(Long catId1, Long catId2);
    void breakeCatsFriendship(Long catId1, Long catId2);
}
