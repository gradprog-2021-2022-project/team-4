package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.repositories.ClothingRepository;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @Autowired
    CommentRepository comments;
    @Autowired
    PostRepository posts;
    @Autowired
    UserRepository users;
    @Autowired
    ClothingRepository clothing;

    @GetMapping("/admin")
    public String adminpage(){
        return "adminpage";
    }
}
