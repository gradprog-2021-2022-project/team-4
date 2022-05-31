package com.irongroup.teamproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Conversation implements Comparable<Conversation> {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convo_generator")
    @SequenceGenerator(name = "convo_generator", sequenceName = "convo_seq", allocationSize = 1)
    @Id
    private Integer id;

    private LocalDateTime lastMessage;

    //Naam voor convo :)
    private String convoNaam;

    //Berichten die tot deze conversatie horen
    //@JsonIgnore
    @OneToMany(mappedBy = "conversation")
    private Collection<Message> messages;

    //Users die tot deze conversatie toegang hebben
    @JsonIgnore
    @ManyToMany
    private Collection<FashUser> users;

    @JsonIgnore
    @ManyToMany
    private List<FashUser> readUsers;

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

    public void addUser(FashUser user) {
        //Eerst kijken of de user collection al besta
        if (this.users != null) {
            this.users.add(user);
        }
        //Zo nee, een nieuwe maken en opnieuw proberen
        else {
            this.users = new ArrayList<FashUser>();
            this.addUser(user);
        }
    }

    public void addMessage(Message message) {
        if (this.messages == null || (this.messages != null && this.messages.size() < 1)) {
            this.messages = new ArrayList<Message>();
            this.messages.add(message);
        } else {
            this.messages.add(message);
        }

    }

    @JsonIgnore
    public LocalDateTime getLastMessage() {
        if (this.lastMessage != null) {
            return lastMessage;
        } else {
            return LocalDateTime.now();
        }
    }

    public void setLastMessage(LocalDateTime lastMessage) {
        this.lastMessage = lastMessage;
    }

    //De convo op read zetten voor een gebruiker
    public void forceOnRead(FashUser user) {
        if (this.readUsers != null) {
            if (this.users.contains(user) && !readUsers.contains(user)) {
                System.out.println("User toegevoegd");
                readUsers.add(user);
            }
        } else if (this.readUsers == null && this.users.contains(user)) {
            System.out.println("nieuw gemaakt en toegevoegd");
            this.readUsers = new ArrayList<FashUser>();
            readUsers.add(user);
        } else {
            //NIKSKE
            System.out.println("Niks gedaan");
        }
    }

    //forceren op gelezen
    public void forceNotRead(FashUser u) {
        if (readUsers != null && readUsers.contains(u)) {
            readUsers.remove(u);
        }
    }

    @JsonIgnore
    public List<FashUser> getReadUsers() {
        return readUsers;
    }

    //Kunnen sorteren op datum
    @Override
    public int compareTo(Conversation o) {
        return o.getLastMessage().compareTo(this.getLastMessage());
    }
}
