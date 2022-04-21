package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FashPost {

    @Id
    public Integer id;

    public FashPost() {
    }
}
