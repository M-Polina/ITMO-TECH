package com.mpolina.cats.repository;

import com.mpolina.cats.entity.owner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OwnerJpaRepository extends JpaRepository<Owner, Long> {
    List<Owner> findByName(String name);
    List<Owner> findBySurname(String surname);
    List<Owner> findByBirthday(Date birthday);
}
