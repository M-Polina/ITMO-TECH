package com.mpolina.cats.entity.cat;

import com.mpolina.cats.entity.owner.Owner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "cat")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "breed")
    private String breed;

    @Enumerated(EnumType.STRING)
    private CatColor color;

    @ManyToOne
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "cat_cat",
            joinColumns = { @JoinColumn(name = "cat_id") },
            inverseJoinColumns = { @JoinColumn(name = "friendcatslist_id") }
    )
    public List<Cat> friendCatsList;

    public Cat(){
        friendCatsList = new ArrayList<Cat>();
    }

    public Cat(String name, Date birthday, String breed, CatColor catColor, Owner owner) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.color = catColor;
        this.owner = owner;
        friendCatsList = new ArrayList<Cat>();
    }

    public String toString() {
        return "Cat(id=" + this.getId() + ", name=" + this.getName() + ", birthday=" + this.getBirthday() + ", breed=" + this.getBreed() + ", color=" + this.getColor() + ", owner=" + this.getOwner() + ")";
    }
}

