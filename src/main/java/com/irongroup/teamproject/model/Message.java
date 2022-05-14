package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {
    @Id
    Integer id;

    //Bij welke conversatie hoort dit bericht?
    @ManyToOne(optional = false)
    private Conversation conversation;

    public Message() {
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
