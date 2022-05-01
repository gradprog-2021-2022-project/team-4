package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    @GetMapping({"/profilepage"})
    public String profilepage(){
        return "profilepage";
    }

    @GetMapping({"/loginerror"})
    public String loginerror(){
        return "loginerror";
    }

    @GetMapping("/logout")
    public String logout(Principal principal, Model model) {
        if (principal == null) return "redirect:/";
        return "user/logout";
    }

    @GetMapping("/login")
    public String login(Principal principal){
        if(principal!=null) return "redirect:/";
        return "user/login";
    }
}
