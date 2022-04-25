package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class FashComment {

    //ID
    @Id
    public Integer id;

    //User that has made the comment
    @OneToOne
    private FashUser poster;

    //Post on which the comment was made
    @OneToOne
    private FashPost post;

    //Basic info of the post itself
    private String title;
    private String text;
    private LocalDate date;
    private LocalTime time;

    public FashComment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FashUser getPoster() {
        return poster;
    }

    public void setPoster(FashUser poster) {
        this.poster = poster;
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
}
