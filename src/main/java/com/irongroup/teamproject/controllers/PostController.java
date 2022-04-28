package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    PostRepository posts;

    @GetMapping({"/explorepage","/"})
    public String explorepage(Model model){
        Collection<FashPost> postsmade=posts.findAll();
        model.addAttribute("fashposts",postsmade);
        return "explorepage";
    }
    @GetMapping({"/foryoupage"})
    public String foryoupage(){
        return "foryoupage";
    }
    @GetMapping({"/postDetails/{id}","postDetails"})
    public String postDetails(Model model, @PathVariable(required = false)Integer id){
        if(id==null) return "postDetails";
        Optional<FashPost> optionalFashPost = posts.findById(id);
        if(optionalFashPost.isPresent()){
            model.addAttribute("post", optionalFashPost.get());
        }
        return "postDetails";
    }
}
