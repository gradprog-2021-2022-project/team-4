package com.irongroup.teamproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
    public String convo(@PathVariable Integer id){
        try {
            return new ObjectMapper().writeValueAsString(convos.findbyID(id).getMessages());
            //ObjectMapper mapper = new ObjectMapper();
            //return mapper.writeValueAsString(convos.findbyID(id).getMessages());
            //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //String json = ow.writeValueAsString(convos.findbyID(id));
            //return json;
        }catch (Exception e){
            e.printStackTrace();
            return "da werkte ni";
        }
    }

    @GetMapping("/testje")
    public String testje(){
        return "dit is een test";
    }
}
