package com.irongroup.teamproject.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Conversation {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convo_generator")
    @SequenceGenerator(name = "convo_generator", sequenceName = "convo_seq", allocationSize = 1)
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

    public void addUser(FashUser user){
        //Eerst kijken of de user collection al besta
        if(this.users!=null){
            this.users.add(user);
        }
        //Zo nee, een nieuwe maken en opnieuw proberen
        else{
            this.users=new ArrayList<FashUser>();
            this.addUser(user);
        }
    }
    public void addMessage(Message message){
        if(this.messages==null || (this.messages!=null && this.messages.size()<1)){
            this.messages=new ArrayList<Message>();
            this.messages.add(message);
        }else{
            this.messages.add(message);
        }

    }
}
