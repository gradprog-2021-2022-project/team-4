package com.irongroup.teamproject.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class FashUser {
    @Id
    public Integer id;
    public String username;

    private String first_name;
    private String last_name;
    private Integer post_allowance;

    @OneToMany
    private List<FashPost> posts;
    @OneToMany
    private List<FashComment> comments;

    public FashUser() {
    }
}
