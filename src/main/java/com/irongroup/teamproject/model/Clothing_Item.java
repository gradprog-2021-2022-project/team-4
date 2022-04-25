package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Clothing_Item {

    //ID
    @Id
    public Integer id;

    //Type kledingstuk (zal misschien een enum worden)
    private String type;

    //Naam van kledingstuk
    private String naam;

    //Prijs van de kleding
    private Double prijs;

    //URL
    private String linkShop;

    public Clothing_Item() {
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
}
