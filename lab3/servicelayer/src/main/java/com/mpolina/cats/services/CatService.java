package com.mpolina.cats.services;

import com.mpolina.cats.dto.CatDto;
import com.mpolina.cats.dto.OwnerDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CatService {
    CatDto createCat(String name, Date birthday, String breed, String strCatColor, Long ownerId);

    CatDto getCatById(Long catId);

    CatDto updateCatInformation(Long catId, String name, Date birthday, String breed, String strCatColor, Long ownerId);

    void deleteCat(Long catId);

    List<CatDto> findAllCats();

    List<CatDto> findCatsByName(String name);

    List<CatDto> findCatsByBirthday(Date birthday);

    List<CatDto> findCatsByColor(String strCatColor);

    List<CatDto> findCatsByBreed(String breed);

    OwnerDto getCatOwner(Long catId);

    List<String> getCatColors();
}
