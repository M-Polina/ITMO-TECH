package com.mpolina.cats.entity.owner;

import com.mpolina.cats.entity.cat.Cat;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthday")
    private Date birthday;

    @OneToMany (mappedBy = "owner")
    private List<Cat> catsList;

    public Owner() {
        catsList = new ArrayList<Cat>();
    }

    public Owner(String name, String surname, Date birthday) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        catsList = new ArrayList<Cat>();
    }

    public String toString() {
        return "Owner(id=" + this.getId() + ", name=" + this.getName() + ", birthday=" + this.getBirthday() + ")";
    }
}

