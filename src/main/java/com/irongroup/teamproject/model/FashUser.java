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
}
