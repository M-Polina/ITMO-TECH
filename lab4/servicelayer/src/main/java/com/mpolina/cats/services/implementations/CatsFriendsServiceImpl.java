package com.mpolina.cats.services.implementations;

import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.exception.CatsLabServiceException;
import com.mpolina.cats.repository.CatJpaRepository;
import com.mpolina.cats.repository.OwnerJpaRepository;
import com.mpolina.cats.services.CatsFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatsFriendsServiceImpl implements CatsFriendsService {

    private final OwnerJpaRepository ownerRepository;

    private final CatJpaRepository catRepository;

    @Autowired
    public CatsFriendsServiceImpl(OwnerJpaRepository ownerRepository,
                                  CatJpaRepository catRepository) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;

    }

    public boolean checkCatsFriendshipPresence(Long catId1, Long catId2) {
        if (catId1 == null || catId2 == null) {
            return false;
        }

        Optional<Cat> foundCatOptional1 = catRepository.findById(catId1);
        if (!foundCatOptional1.isPresent()) {
            return false;
        }

        Optional<Cat> foundCatOptional2 = catRepository.findById(catId2);
        if (!foundCatOptional2.isPresent()) {
            return false;
        }

        boolean cat1HasCat2AsFriend = foundCatOptional1.get().getFriendCatsList().stream().anyMatch(c -> c.getId() == catId2);
        boolean cat2HasCat1AsFriend = foundCatOptional2.get().getFriendCatsList().stream().anyMatch(c -> c.getId() == catId1);

        if (cat1HasCat2AsFriend && cat2HasCat1AsFriend) {
            return true;
        }

        return false;
    }

    public void makeCatsFriends(Long catId1, Long catId2) {
        if (catId1 == null || catId2 == null) {
            throw new CatsLabServiceException("Null id while making cats friends.");
        }

        Optional<Cat> foundCatOptional1 = catRepository.findById(catId1);
        if (!foundCatOptional1.isPresent()) {
            throw new CatsLabServiceException("No cat with this Id while making cats friends.");
        }
        Cat foundCat1 = foundCatOptional1.get();

        Optional<Cat> foundCatOptional2 = catRepository.findById(catId2);
        if (!foundCatOptional2.isPresent()) {
            throw new CatsLabServiceException("No cat with this Id while making cats friends");
        }
        Cat foundCat2 = foundCatOptional2.get();

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId2);
        boolean cat2HasCat1AsFriend = foundCat2.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId1);

        if (cat1HasCat2AsFriend && cat2HasCat1AsFriend) {
            throw new CatsLabServiceException("Cats already are friends while making cats friends.");
        }

        if (cat1HasCat2AsFriend || cat2HasCat1AsFriend) {
            throw new CatsLabServiceException("Cats are not mutual friends while making cats friends.");
        }

        foundCat1.getFriendCatsList().add(foundCat2);
        foundCat2.getFriendCatsList().add(foundCat1);

        catRepository.saveAndFlush(foundCat1);
        catRepository.saveAndFlush(foundCat2);
    }

    public void breakeCatsFriendship(Long catId1, Long catId2) {
        if (catId1 == null || catId2 == null) {
            throw new CatsLabServiceException("Null id while braking cats friendship.");
        }

        Optional<Cat> foundCatOptional1 = catRepository.findById(catId1);
        if (!foundCatOptional1.isPresent()) {
            throw new CatsLabServiceException("No cat with this first Id while braking cats friendship.");
        }
        Cat foundCat1 = foundCatOptional1.get();

        Optional<Cat> foundCatOptional2 = catRepository.findById(catId2);
        if (!foundCatOptional2.isPresent()) {
            throw new CatsLabServiceException("No cat with this second Id while braking cats friendship.");
        }
        Cat foundCat2 = foundCatOptional2.get();

        boolean cat1HasCat2AsFriend = foundCat1.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId2);
        boolean cat2HasCat1AsFriend = foundCat2.getFriendCatsList().stream().anyMatch(c -> c.getId() == catId1);

        if ((!cat1HasCat2AsFriend) || (!cat2HasCat1AsFriend)) {
            throw new CatsLabServiceException("Cats are not friends  while braking cats friendship.");
        }

        if (!cat1HasCat2AsFriend) {
            throw new CatsLabServiceException("First cat hasn't got second as friend  while braking cats friendship.");
        }

        if (!cat2HasCat1AsFriend) {
            throw new CatsLabServiceException("Second cat hasn't got first as friend while braking cats friendship.");
        }

        foundCat1.getFriendCatsList().removeIf(c -> (c.getId() == foundCat2.getId()));
        foundCat2.getFriendCatsList().removeIf(c -> (c.getId() == foundCat1.getId()));

        catRepository.saveAndFlush(foundCat1);
        catRepository.saveAndFlush(foundCat2);
    }
}
