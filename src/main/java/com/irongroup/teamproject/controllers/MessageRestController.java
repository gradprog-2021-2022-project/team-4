package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Conversation;
import com.irongroup.teamproject.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageRestController {
    @Autowired
    ConversationRepository convos;

    @GetMapping("/getconvo/{id}")
    public Conversation convo(@PathVariable Integer id){
        return convos.findbyID(id);
    }
}
