package com.irongroup.teamproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
    @GetMapping({"/explorepage"})
    public String explorepage(){
        return "explorepage";
    }
    @GetMapping({"/foryoupage"})
    public String foryoupage(){
        return "foryoupage";
    }
}
