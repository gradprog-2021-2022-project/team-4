package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Clothing_Item {

    @Id
    public Integer id;

    public Clothing_Item() {
    }
}
