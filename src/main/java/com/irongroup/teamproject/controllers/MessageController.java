package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.ConversationRepository;
import com.irongroup.teamproject.repositories.UserRepository;
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

    @GetMapping("/messages/{id}")
    public String conversation(Principal p, Model model, @PathVariable Integer id) {
        //In een try catch= geen ifke
        try {
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            model.addAttribute("convo",convos.findbyID(id));
            model.addAttribute("loggedUser",loggedIn);
            return "user/conversation";
        } catch (Exception e) {
            return "redirect:/explorepage";
        }
    }
}
