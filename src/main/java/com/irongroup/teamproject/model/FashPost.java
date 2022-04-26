package com.irongroup.teamproject.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Entity
public class FashPost {

    //ID
    @Id
    public Integer id;


    //Clothes in the post
    @OneToMany(mappedBy = "post")
    private Collection<Clothing_Item> clothes;

    //Poster
    @ManyToOne
    private FashUser poster;

    //All comments on the post
    @OneToMany(mappedBy = "post")
    private Collection<FashComment> comments;

    //Date and time of the post
    private LocalDate date;
    private LocalTime time;

    //Location of post (to be investigated)
    private String location;

    public FashPost() {
    }

    public FashPost(Integer id, Collection<Clothing_Item> clothes, FashUser poster, Collection<FashComment> comments, LocalDate date, LocalTime time, String location) {
        this.id = id;
        this.clothes = clothes;
        this.poster = poster;
        this.comments = comments;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Collection<Clothing_Item> getClothes() {
        return clothes;
    }

    public void setClothes(Collection<Clothing_Item> clothes) {
        this.clothes = clothes;
    }

    public FashUser getPoster() {
        return poster;
    }

    public void setPoster(FashUser poster) {
        this.poster = poster;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setComments(Collection<FashComment> comments) {
        this.comments = comments;
    }
}
