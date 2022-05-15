package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Conversation;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.ConversationRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class MessageController {
    @Autowired
    UserRepository users;
    @Autowired
    ConversationRepository convos;

    // TODO : checks toevoegen aub eliasje
    @GetMapping("/messages")
    public String messagelist(Principal p, Model model) {
        //In een try catch= geen ifke
        try {
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            model.addAttribute("convos",loggedIn.getConversations());
            return "user/messagelist";
        } catch (Exception e) {
            return "redirect:/explorepage";
        }
    }

    // done : voeg een check toe om te zien of de gebruiker toegang heeft tot de convo!!
    @GetMapping("/messages/{id}")
    public String conversation(Principal p, Model model, @PathVariable Integer id) {
        //In een try catch= geen ifke
        try {
            //Gebruiker vinden die ingelogd is
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            //Convo vinden die gevraagd word
            Conversation conversation=convos.findbyID(id);
            if(conversation.getUsers().contains(loggedIn)){
                //Toevoegen aan het model enkel als de gebruiker toegang heeft tot de convo
                model.addAttribute("convo",conversation);
                model.addAttribute("loggedUser",loggedIn);
                return "user/conversation";
            }
            else {return "redirect:/explorepage";}
        } catch (Exception e) {
            return "redirect:/explorepage";
        }
    }
}
