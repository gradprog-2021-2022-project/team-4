package com.irongroup.teamproject.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class FashComment {

    //ID
    @Id
    public Integer id;

    //User that has made the comment
    @ManyToOne(optional = false)
    private FashUser user;

    //Post on which the comment was made
    @ManyToOne
    private FashPost post;

    //Basic info of the post itself
    private String title;
    private String text;
    private LocalDate date;
    private LocalTime time;

    public FashComment() {
    }

    public FashComment(Integer id, FashUser user, FashPost post, String title, String text, LocalDate date, LocalTime time) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.title = title;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FashPost getPost() {
        return post;
    }

    public void setPost(FashPost post) {
        this.post = post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public FashUser getUser() {
        return user;
    }

    public void setUser(FashUser user) {
        this.user = user;
    }
}
