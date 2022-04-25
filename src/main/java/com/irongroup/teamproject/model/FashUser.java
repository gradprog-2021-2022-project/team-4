package com.irongroup.teamproject.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class FashUser {
    //ID and username are public for all users.
    @Id
    public Integer id;
    public String username;

    //These are private unless user wants to share
    private String first_name;
    private String last_name;
    private Integer post_allowance;

    //Posts and comments user has made
    @OneToMany
    private List<FashPost> posts;
    @OneToMany
    private List<FashComment> comments;

    //Clothing that the user has posted and saved
    @OneToMany
    private List<Clothing_Item> clothing_posted;
    @ManyToMany
    private List<Clothing_Item> clothing_saved;


    public FashUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getPost_allowance() {
        return post_allowance;
    }

    public void setPost_allowance(Integer post_allowance) {
        this.post_allowance = post_allowance;
    }

    public List<FashPost> getPosts() {
        return posts;
    }

    public void setPosts(List<FashPost> posts) {
        this.posts = posts;
    }

    public List<FashComment> getComments() {
        return comments;
    }

    public void setComments(List<FashComment> comments) {
        this.comments = comments;
    }

    public List<Clothing_Item> getClothing_posted() {
        return clothing_posted;
    }

    public void setClothing_posted(List<Clothing_Item> clothing_posted) {
        this.clothing_posted = clothing_posted;
    }

    public List<Clothing_Item> getClothing_saved() {
        return clothing_saved;
    }

    public void setClothing_saved(List<Clothing_Item> clothing_saved) {
        this.clothing_saved = clothing_saved;
    }
}
