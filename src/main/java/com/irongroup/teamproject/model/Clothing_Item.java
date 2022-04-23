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
}
