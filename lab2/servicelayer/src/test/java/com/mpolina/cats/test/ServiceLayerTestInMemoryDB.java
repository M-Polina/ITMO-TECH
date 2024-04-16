package com.mpolina.cats.test;

import com.mpolina.cats.dao.CatDao;
import com.mpolina.cats.dao.OwnerDao;
import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.CatsFriendsService;
import com.mpolina.cats.services.OwnerService;
import com.mpolina.cats.util.HibernateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

public class ServiceLayerTestInMemoryDB {
    private CatDao catDao;

    private OwnerDao ownerDao;

    private CatService catService;

    private OwnerService ownerService;

    private CatsFriendsService catsFriendsService;

    public ServiceLayerTestInMemoryDB() {
        catDao = new CatDao();
        ownerDao = new OwnerDao();
        this.catService = new CatService(catDao, ownerDao);
        this.ownerService = new OwnerService(catDao, ownerDao);
        this.catsFriendsService = new CatsFriendsService(catDao, ownerDao);
    }

    @BeforeEach
    public void initEach(){
        HibernateUtil.closeSessionFactory();
        HibernateUtil.openSessionFactory();
    }

    @Test
    public void checkOwnerPresenceAfterCreating() {
        OwnerDto ownerDto = ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));

        Assertions.assertEquals(1, ownerService.getAllOwners().size());
        Assertions.assertEquals(ownerDto.ownerId(), ownerService.getAllOwners().get(0).ownerId());
    }

    @Test
    public void checkCatPresenceAfterCreating() {
        OwnerDto ownerDto = ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));

        CatDto catDto = catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);

        Assertions.assertEquals(1, catService.getAllCats().size());
        Assertions.assertEquals(catDto.catId(), catService.getAllCats().get(0).catId());
        Assertions.assertEquals("Kitti", catService.getAllCats().get(0).name());
        Assertions.assertEquals(new Date(2000, 01, 01), catService.getAllCats().get(0).birthday());
        Assertions.assertEquals("Russian blue", catService.getAllCats().get(0).breed());
        Assertions.assertEquals("GREY", catService.getAllCats().get(0).color());
        Assertions.assertEquals(catDto.ownerId(), catService.getAllCats().get(0).ownerId());
    }

    @Test
    public void checkCatFriendshipAfterCreating() {
        OwnerDto ownerDto = ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));

        catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catService.createCat("Milka", new Date(2001, 01, 01), "Russian blue", "GREY", 1L);

        catsFriendsService.makeCatsFriends(1L, 2L);

        CatDto catDto1 = catService.findCatById(1L);
        CatDto catDto2 = catService.findCatById(2L);

        Assertions.assertTrue(catDto1.catFriendsIdList().get(0) == 2L);
        Assertions.assertTrue(catDto2.catFriendsIdList().get(0) == 1L);
    }

    @Test
    public void checkCatsNotFriendsAfterCreatingThenDeletingFrienship() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));

        catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catService.createCat("Milka", new Date(2001, 01, 01), "Russian blue", "GREY", 1L);

        catsFriendsService.makeCatsFriends(1L, 2L);
        catsFriendsService.breakeCatsFriendship(1L, 2L);

        CatDto catDto1 = catService.findCatById(1L);
        CatDto catDto2 = catService.findCatById(2L);

        Assertions.assertEquals(0, catDto1.catFriendsIdList().size());
        Assertions.assertEquals(0, catDto2.catFriendsIdList().size());
    }

    @Test
    public void checkAbsenceOfOwnerAfterDeletion() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));

        ownerService.deleteOwner(1L);
        Assertions.assertEquals(0, ownerService.getAllOwners().size());
    }

    @Test
    public void checkAbsenceOfCatAfterDeletion() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));
        catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);

        catService.deleteCat(1L);
        Assertions.assertEquals(0, catService.getAllCats().size());
    }

    @Test
    public void checkAbsenceOfCatInFriendshipListOfCatsFriendAfterDeletion() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));
        catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catService.createCat("Milka", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catsFriendsService.makeCatsFriends(1L, 2L);

        catService.deleteCat(1L);

        Assertions.assertEquals(0, catService.findCatById(2L).catFriendsIdList().size());
        Assertions.assertNull(catService.findCatById(1L));
    }

    @Test
    public void checkOwnerInfoIsNewAfterUpdatingUpdating() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));
        ownerService.updateOwnerInformation(1L, "Den", "Lidvord", new Date(2001, 01, 01));

        OwnerDto ownerDto = ownerService.findOwnerById(1L);

        Assertions.assertEquals("Den", ownerDto.name());
        Assertions.assertEquals("Lidvord", ownerDto.surname());
        Assertions.assertEquals(new Date(2001, 01, 01), ownerDto.birthday());
    }

    @Test
    public void checkCatInfoIsNewAfterUpdatingUpdating() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));
        catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);

        catService.updateCatInformation(1L, "Milka",  new Date(2001, 01, 01), "Russian blue", "BLACK", 1L);
        CatDto catDto = catService.findCatById(1L);

        Assertions.assertEquals("Milka", catDto.name());
        Assertions.assertEquals(new Date(2001, 01, 01), catDto.birthday());
        Assertions.assertEquals("Russian blue", catDto.breed());
        Assertions.assertEquals("BLACK", catDto.color());
        Assertions.assertEquals(1L, catDto.ownerId());
    }

    @Test
    public void checkFindByPredicateGetAllGreyCats() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));
        catService.createCat("Kitti1", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catService.createCat("Kitti2", new Date(2000, 01, 01), "Russian blue", "BLACK", 1L);
        catService.createCat("Kitti3", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catService.createCat("Kitti4", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);
        catService.createCat("Kitti5", new Date(2000, 01, 01), "Russian blue", "AMBER", 1L);

       List<CatDto> catsDtoList = catService.getCatsByPredicate(c -> c.getColor().toString().equals("GREY"));

        Assertions.assertEquals(3, catsDtoList.size());
        Assertions.assertEquals("Kitti1", catsDtoList.get(0).name());
        Assertions.assertEquals("Kitti3", catsDtoList.get(1).name());
        Assertions.assertEquals("Kitti4", catsDtoList.get(2).name());
    }

    @Test
    public void checkFindByPredicateGetAllOwnersDen() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));
        ownerService.createOwner("Danny", "Renidi", new Date(2000, 01, 01));
        ownerService.createOwner("Den", "Menidi", new Date(2000, 01, 01));
        ownerService.createOwner("Den", "Fenidi", new Date(2000, 01, 01));

        List<OwnerDto> ownersDtoList = ownerService.getOwnersByOwnerPredicate(o -> o.getName().equals("Den"));

        Assertions.assertEquals(3, ownersDtoList.size());
        Assertions.assertEquals("Lenidi", ownersDtoList.get(0).surname());
        Assertions.assertEquals("Menidi", ownersDtoList.get(1).surname());
        Assertions.assertEquals("Fenidi", ownersDtoList.get(2).surname());
    }
}
