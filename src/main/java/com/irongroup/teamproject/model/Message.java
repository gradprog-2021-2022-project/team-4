package com.irongroup.teamproject.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Message {
    @Id
    Integer id;

    //Bij welke conversatie hoort dit bericht?
    @ManyToOne(optional = false)
    private Conversation conversation;

    //De persoon die het bericht verstuurde
    @ManyToOne
    private FashUser sender;

    //Mensen die het bericht zullen ontvangen
    @ManyToMany
    private Collection<FashUser> receivers;

    //De tekst van het bericht!!
    private String text;

    public Message() {
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
}
