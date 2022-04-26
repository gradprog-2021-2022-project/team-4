package com.irongroup.teamproject.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Collection;
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
    @OneToMany(mappedBy = "poster")
    private Collection<FashPost> postsMade;
    @OneToMany(mappedBy = "user")
    private List<FashComment> comments;

    //Clothing that the user has posted and saved
    @OneToMany(mappedBy = "userOwner")
    private List<Clothing_Item> clothing_posted;
    @ManyToMany
    private List<Clothing_Item> clothing_saved;


    public FashUser() {
    }

    public FashUser(Integer id, String username, String first_name, String last_name, Integer post_allowance, Collection<FashPost> postsMade, List<FashComment> comments, List<Clothing_Item> clothing_posted, List<Clothing_Item> clothing_saved) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.post_allowance = post_allowance;
        this.postsMade = postsMade;
        this.comments = comments;
        this.clothing_posted = clothing_posted;
        this.clothing_saved = clothing_saved;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Integer getPost_allowance() {
        return post_allowance;
    }

    public Collection<FashPost> getPostsMade() {
        return postsMade;
    }

    public List<FashComment> getComments() {
        return comments;
    }

    public List<Clothing_Item> getClothing_posted() {
        return clothing_posted;
    }

    public List<Clothing_Item> getClothing_saved() {
        return clothing_saved;
    }
}
