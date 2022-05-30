package com.irongroup.teamproject.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Clothing_Item implements Comparable<Clothing_Item>{

    //ID
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clothing_generator")
    @SequenceGenerator(name = "clothing_generator", sequenceName = "clothing_seq",allocationSize = 1)
    @Id
    public Integer id;

    //Type kledingstuk (zal misschien een enum worden)
    private String type;

    //Naam van kledingstuk
    private String naam;

    //Prijs van de kleding
    private Double prijs;

    //URL
    @Column(length=1000)
    private String linkShop;


    @ManyToOne(optional = true)
    private FashPost post;


    @ManyToMany(mappedBy = "clothing_saved", cascade = CascadeType.REMOVE)
    private Collection<FashUser> fashUsers;

    @ManyToOne(optional = false)
    private FashUser userOwner;

    public Clothing_Item() {
    }

    public Clothing_Item(Integer id, String type, String naam, Double prijs, String linkShop, FashPost post, Collection<FashUser> fashUsers, FashUser userOwner) {
        this.id = id;
        this.type = type;
        this.naam = naam;
        this.prijs = prijs;
        this.linkShop = linkShop;
        this.post = post;
        this.fashUsers = fashUsers;
        this.userOwner = userOwner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }

    public String getLinkShop() {
        return linkShop;
    }

    public void setLinkShop(String linkShop) {
        this.linkShop = linkShop;
    }

    public FashPost getPost() {
        return post;
    }

    public void setPost(FashPost post) {
        this.post = post;
    }

    public Collection<FashUser> getFashUsers() {
        return fashUsers;
    }

    public void setFashUsers(Collection<FashUser> fashUsers) {
        this.fashUsers = fashUsers;
    }

    public FashUser getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(FashUser userOwner) {
        this.userOwner = userOwner;
    }

    //Comparable voor sorteren op datum!
    @Override
    public int compareTo(Clothing_Item o) {
        return o.getPost().getDate().compareTo(this.getPost().getDate());
    }
}
