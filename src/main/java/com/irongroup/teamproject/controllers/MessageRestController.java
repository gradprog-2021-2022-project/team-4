package com.irongroup.teamproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public String convo(@PathVariable Integer id){
        try {
            //Dit zet een object om naar een json formaat
            return new ObjectMapper().writeValueAsString(convos.findbyID(id).getMessages());
        }catch (Exception e){
            //Enkel printen bij debuggen
            //e.printStackTrace();
            return "da werkte ni";
        }
    }

    @GetMapping("/testje")
    public String testje(){
        return "dit is een test";
    }
}
