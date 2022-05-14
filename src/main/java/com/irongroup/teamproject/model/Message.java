package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {
    @Id
    Integer id;

    public Message() {
    }
}
