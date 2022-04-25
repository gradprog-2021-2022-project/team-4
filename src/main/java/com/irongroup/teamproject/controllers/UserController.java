package com.irongroup.teamproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping({"/profilepage"})
    public String profilepage(){
        return "profilepage";
    }
}
