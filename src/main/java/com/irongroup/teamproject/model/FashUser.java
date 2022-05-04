package com.irongroup.teamproject.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class FashUser {
    //ID and username are public for all users.
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq",allocationSize = 1)
    @Id
    public Integer id;
    @NotBlank
    public String username;
    private String location;
    @NotBlank
    private String password;

    private File file;

    private String type = "png";
    private byte[] profilePic;

    private String photos;

    private String role;
    //These are private unless user wants to share
    private String first_name;
    private String last_name;
    private Integer post_allowance;

    //Posts and comments user has made
    @OneToMany(mappedBy = "poster", cascade = CascadeType.REMOVE)
    private Collection<FashPost> postsMade;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<FashComment> comments;

    //Clothing that the user has posted and saved
    @OneToMany(mappedBy = "userOwner")
    private List<Clothing_Item> clothing_posted;
    @ManyToMany
    private List<Clothing_Item> clothing_saved;


    public FashUser() {
    }

    public FashUser(Integer id, String username, String location, String first_name, String last_name, Integer post_allowance, Collection<FashPost> postsMade, List<FashComment> comments, List<Clothing_Item> clothing_posted, List<Clothing_Item> clothing_saved, String password, String role) {
        this.id = id;
        this.username = username;
        this.location = location;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.password = password;
        this.post_allowance = post_allowance;
        this.postsMade = postsMade;
        this.comments = comments;
        this.clothing_posted = clothing_posted;
        this.clothing_saved = clothing_saved;
    }

    public File getFile() {
        return file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPost_allowance(Integer post_allowance) {
        this.post_allowance = post_allowance;
    }

    public void setPostsMade(Collection<FashPost> postsMade) {
        this.postsMade = postsMade;
    }

    public void setComments(List<FashComment> comments) {
        this.comments = comments;
    }

    public void setClothing_posted(List<Clothing_Item> clothing_posted) {
        this.clothing_posted = clothing_posted;
    }

    public void setClothing_saved(List<Clothing_Item> clothing_saved) {
        this.clothing_saved = clothing_saved;
    }

    public FashPost getLastPost(){
        ArrayList<FashPost> posts=new ArrayList<FashPost>();
        for (FashPost p:postsMade
             ) {
            posts.add(p);
        }
        return posts.get(posts.size()-1);
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotos() {
        return photos;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/src/main/resources/static/user-photos/" + id + "/" + photos;
    }
}
