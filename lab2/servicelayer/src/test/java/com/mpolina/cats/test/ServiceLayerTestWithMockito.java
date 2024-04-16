package com.mpolina.cats.test;

import com.mpolina.cats.dao.CatDao;
import com.mpolina.cats.dao.OwnerDao;
import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.cat.CatColor;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.services.CatService;
import com.mpolina.cats.services.CatsFriendsService;
import com.mpolina.cats.services.OwnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Date;
import java.util.Objects;

import static org.mockito.BDDMockito.given;

public class ServiceLayerTestWithMockito {
    @Mock
    private CatDao catDao;

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private CatService catService;

    @InjectMocks
    private OwnerService ownerService;

    @InjectMocks
    private CatsFriendsService catsFriendsService;

    public ServiceLayerTestWithMockito() {
        MockitoAnnotations.initMocks(this);
        this.catService = new CatService(catDao, ownerDao);
        this.ownerService = new OwnerService(catDao, ownerDao);
        this.catsFriendsService = new CatsFriendsService(catDao, ownerDao);
    }

    @Test
    public void checkOwnerPresenceAfterCreating() {
        ownerService.createOwner("Den", "Lenidi", new Date(2000, 01, 01));

        Mockito.verify(ownerDao).save(ArgumentMatchers.argThat(o ->
                Objects.equals(new Date(2000, 01, 01), o.getBirthday())
                        && Objects.equals("Den", o.getName())
                        && Objects.equals("Lenidi", o.getSurname())));
    }

    @Test
    public void checkCatPresenceAfterCreating() {
        Owner owner = new Owner("Den", "Lenidi", new Date(2006 - 4 - 4));
        owner.setId(1L);
        given(ownerDao.findById(1L)).willReturn(owner);

        CatDto catDto = catService.createCat("Kitti", new Date(2000, 01, 01), "Russian blue", "GREY", 1L);

        Mockito.verify(catDao).save(ArgumentMatchers.argThat(c ->
                Objects.equals(catDto.birthday(), c.getBirthday())
                        && Objects.equals(catDto.name(), c.getName())
                        && Objects.equals(catDto.breed(), c.getBreed())
                        && Objects.equals(catDto.color(), c.getColor().toString())
                        && Objects.equals(catDto.ownerId(), c.getOwner().getId())));
    }

    @Test
    public void checkCatFriendshipAfterCreating() {
        Owner owner = new Owner("Den", "Lenidi", new Date(2006 - 4 - 4));
        owner.setId(1L);
        given(ownerDao.findById(1L)).willReturn(owner);

        Cat cat1 = new Cat("Kitti", new Date(2000, 01, 01), "Russian blue", CatColor.GREY, owner);
        cat1.setId(1L);
        given(catDao.findById(1L)).willReturn(cat1);

        Cat cat2 = new Cat("Milka", new Date(2001, 01, 01), "Russian blue", CatColor.GREY, owner);
        cat2.setId(2L);
        given(catDao.findById(2L)).willReturn(cat2);

        catsFriendsService.makeCatsFriends(1L, 2L);

        CatDto catDto1 = catService.findCatById(1L);
        CatDto catDto2 = catService.findCatById(2L);

        Assertions.assertTrue(catDto1.catFriendsIdList().get(0) == 2L);
        Assertions.assertTrue(catDto2.catFriendsIdList().get(0) == 1L);
    }

    @Test
    public void checkCatsNotFriendsAfterCreatingThenDeletingFrienship() {
        Owner owner = new Owner("Den", "Lenidi", new Date(2006 - 4 - 4));
        owner.setId(1L);
        given(ownerDao.findById(1L)).willReturn(owner);

        Cat cat1 = new Cat("Kitti", new Date(2000, 01, 01), "Russian blue", CatColor.GREY, owner);
        cat1.setId(1L);
        given(catDao.findById(1L)).willReturn(cat1);

        Cat cat2 = new Cat("Milka", new Date(2001, 01, 01), "Russian blue", CatColor.GREY, owner);
        cat2.setId(2L);
        given(catDao.findById(2L)).willReturn(cat2);

        catsFriendsService.makeCatsFriends(1L, 2L);
        catsFriendsService.breakeCatsFriendship(1L, 2L);

        CatDto catDto1 = catService.findCatById(1L);
        CatDto catDto2 = catService.findCatById(2L);

        Assertions.assertEquals(0, catDto1.catFriendsIdList().size());
        Assertions.assertEquals(0, catDto2.catFriendsIdList().size());
    }
}
