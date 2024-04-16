package com.mpolina.cats.repository;

import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.cat.CatColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CatJpaRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByName(String name);
    List<Cat> findByBirthday(Date birthday);
    List<Cat> findByBreed(String breed);
    List<Cat> findByColor(CatColor color);
}
