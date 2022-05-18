package com.irongroup.teamproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Message {
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mes_generator")
    //@SequenceGenerator(name = "mes_generator", sequenceName = "mes_seq", allocationSize = 1)
    @Id
    Integer id;

    //Bij welke conversatie hoort dit bericht?
    //JSON ignore betekent dat dit niet word meegelezen in de json compiler
    @JsonIgnore
    @ManyToOne(optional = false)
    private Conversation conversation;

    //De persoon die het bericht verstuurde
    //@JsonIgnore
    @ManyToOne
    private FashUser sender;

    //Mensen die het bericht zullen ontvangen
    @JsonIgnore
    @ManyToMany
    private Collection<FashUser> receivers;

    //De tekst van het bericht!!
    private String text;

    public Message() {
    }

    public Message(Integer id, Conversation conversation, FashUser sender, Collection<FashUser> receivers, String text) {
        this.id = id;
        this.conversation = conversation;
        this.sender = sender;
        this.receivers = receivers;
        this.text = text;
    }

    public Message(Conversation conversation, FashUser sender, Collection<FashUser> receivers, String text) {
        this.conversation = conversation;
        this.sender = sender;
        this.receivers = receivers;
        this.text = text;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Integer getId() {
        return id;
    }

    public FashUser getSender() {
        return sender;
    }

    public Collection<FashUser> getReceivers() {
        return receivers;
    }

    public String getText() {
        return text;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSender(FashUser sender) {
        this.sender = sender;
    }

    public void setReceivers(Collection<FashUser> receivers) {
        this.receivers = receivers;
    }

    public void setText(String text) {
        this.text = text;
    }
}
