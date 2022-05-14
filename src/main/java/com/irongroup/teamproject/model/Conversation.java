package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class Conversation {
    @Id
    private Integer id;

    //Berichten die tot deze conversatie horen
    @OneToMany(mappedBy = "conversation")
    private Collection<Message> messages;

    //Users die tot deze conversatie toegang hebben
    @ManyToMany
    private Collection<FashUser> users;
}
