package com.irongroup.teamproject.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class FashPost {

    //ID
    @Id
    public Integer id;

    //Clothes in the post
    @OneToMany
    private List<Clothing_Item> clothes;

    //Poster
    @ManyToOne
    private FashUser poster;

    //All comments on the post
    @OneToMany
    private List<FashPost> comments;

    //Date and time of the post
    private LocalDate date;
    private LocalTime time;

    //Location of post (to be investigated)
    private String location;

    public FashPost() {
    }
}
