package com.mpolina.cats.services;

import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CatService {
    CatDto createCat(String name, Date birthday, String breed, String strCatColor, Long ownerId);

    CatDto getCatById(Long catId, String username);

    CatDto updateCatInformation(Long catId, String name, Date birthday, String breed, String strCatColor, Long ownerId);

    void deleteCat(Long catId);

    List<CatDto> findAllCats(String username);

    List<CatDto> findCatsByName(String name, String username);

    List<CatDto> findCatsByBirthday(Date birthday, String username);

    List<CatDto> findCatsByColor(String strCatColor, String username);

    List<CatDto> findCatsByBreed(String breed, String username);

    OwnerDto getCatOwner(Long catId, String username);

    List<String> getCatColors();
}
