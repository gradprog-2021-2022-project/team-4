package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MessageController {
    @Autowired
    UserRepository users;

    @GetMapping("/messages")
    public String messagelist(Principal p) {
        //In een try catch= geen ifke
        try {
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            return "user/messagelist";
        } catch (Exception e) {
            return "redirect:/explorepage";
        }
    }

    @GetMapping("/messages/{id}")
    public String conversation(Principal p) {
        //In een try catch= geen ifke
        try {
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            return "user/conversation";
        } catch (Exception e) {
            return "redirect:/explorepage";
        }
    }
}
