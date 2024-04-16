package com.mpolina.cats.entity.owner;

import com.mpolina.cats.entity.cat.Cat;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "OWNER")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "BIRTHDAY")
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
