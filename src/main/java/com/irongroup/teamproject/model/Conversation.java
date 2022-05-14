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

    //Naam voor convo :)
    private String convoNaam;

    //Berichten die tot deze conversatie horen
    @OneToMany(mappedBy = "conversation")
    private Collection<Message> messages;

    //Users die tot deze conversatie toegang hebben
    @ManyToMany
    private Collection<FashUser> users;

    public Conversation() {
    }

    public Conversation(Integer id, String convoNaam, Collection<Message> messages, Collection<FashUser> users) {
        this.id = id;
        this.convoNaam = convoNaam;
        this.messages = messages;
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public String getConvoNaam() {
        return convoNaam;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public Collection<FashUser> getUsers() {
        return users;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setConvoNaam(String convoNaam) {
        this.convoNaam = convoNaam;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    public void setUsers(Collection<FashUser> users) {
        this.users = users;
    }
}
