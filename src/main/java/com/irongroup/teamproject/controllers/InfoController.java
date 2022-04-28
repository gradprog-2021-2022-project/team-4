package com.irongroup.teamproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {
    @GetMapping({"/infopage"})
    public String infopage(){
        return "infopage";
    }
}
