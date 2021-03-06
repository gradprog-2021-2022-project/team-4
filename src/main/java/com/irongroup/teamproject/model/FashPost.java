package com.irongroup.teamproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class FashPost implements Comparable<FashPost>{

    //ID
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_generator")
    @SequenceGenerator(name = "post_generator", sequenceName = "post_seq",allocationSize = 1)
    @Id
    public Integer id;
    @Column(length = 1000)
    private String text;
    private String stijl;

    //Clothes in the post
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Collection<Clothing_Item> clothes = new ArrayList<>();

    //Poster
    @JsonIgnore
    @ManyToOne
    private FashUser poster;

    //All comments on the post
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Collection<FashComment> comments;

    @JsonIgnore
    @Column(columnDefinition="BLOB")
    private byte[] postPic;

    //Date and time of the post
    @JsonIgnore
    private LocalDate date;
    @JsonIgnore
    private LocalTime time;

    @JsonIgnore
    public LocalDateTime getDateTime(){
        return LocalDateTime.of(this.date,this.time);
    }

    //Location of post (to be investigated)
    private String location;

    public FashPost() {
    }

    public FashPost(Integer id, String stijl, Collection<Clothing_Item> clothes, FashUser poster, Collection<FashComment> comments, LocalDate date, LocalTime time, String location, String text, byte[] postPic) {
        this.id = id;
        this.stijl = stijl;
        this.clothes = clothes;
        this.poster = poster;
        this.comments = comments;
        this.date = date;
        this.time = time;
        this.location = location;
        this.text=text;
        this.postPic=postPic;
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

    public void setStijl(String stijl) {
        this.stijl = stijl;
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

    public Collection<FashComment> getComments() {
        return comments;
    }

    public FashComment getOneComment() {
        if(comments.isEmpty()){
            return null;
        }

        ArrayList<FashComment> comments=new ArrayList<FashComment>();
        if (this.comments.isEmpty()) {
            return null;
        }
        else {
            for (FashComment c:this.comments) {
                comments.add(c);
            }
            return comments.get(comments.size()-1);
        }
    }

    public String getText() {
        return text;
    }

    public byte[] getPostPic() {
        return postPic;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setPostPic(byte[] postPic) {
        this.postPic = postPic;
    }

    public String getStijl() {
        return stijl;
    }
    public Double getTotalPrice(){
        Double totaal=0.0;
        for (Clothing_Item c:clothes
             ) {
            totaal+=c.getPrijs();
        }
        return totaal;
    }

    //Compare voor sorteren
    @Override
    public int compareTo(FashPost other){
        return other.getDateTime().compareTo(this.getDateTime());
    }
}