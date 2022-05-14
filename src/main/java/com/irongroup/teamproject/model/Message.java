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
    FashUser sender;

    //Mensen die het bericht zullen ontvangen
    @ManyToMany
    Collection<FashUser> receivers;

    public Message() {
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
