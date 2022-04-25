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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Clothing_Item> getClothes() {
        return clothes;
    }

    public void setClothes(List<Clothing_Item> clothes) {
        this.clothes = clothes;
    }

    public FashUser getPoster() {
        return poster;
    }

    public void setPoster(FashUser poster) {
        this.poster = poster;
    }

    public List<FashPost> getComments() {
        return comments;
    }

    public void setComments(List<FashPost> comments) {
        this.comments = comments;
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
}
