package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class PostController {
    @Autowired
    PostRepository posts;

    @GetMapping({"/explorepage"})
    public String explorepage(Model model){
        Collection<FashPost> postsmade=posts.findAll();
        model.addAttribute("fashposts",postsmade);
        return "explorepage";
    }
    @GetMapping({"/foryoupage"})
    public String foryoupage(){
        return "foryoupage";
    }
}
