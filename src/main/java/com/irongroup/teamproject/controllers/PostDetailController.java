package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashComment;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Controller
public class PostDetailController {
    @Autowired
    PostRepository posts;
    @Autowired
    UserRepository users;
    @Autowired
    CommentRepository comments;

    @GetMapping({"/postDetails/{id}", "/postDetails"})
    public String postDetails(Model model, @PathVariable(required = false) Integer id, Principal
            principal, @RequestParam(required = false) String commentText, @RequestParam(required = false) String
                                      commentTitle) {
        try {
            //Kijken of je aangemeld bent;
            //boolean aangemeld= principal != null;
            //Als de gebruiker niet aangemeld is, kan je geen comments plaatsen
            if (principal != null) {
                FashUser loggedInUser = users.findFashUserByUsername(principal.getName());
                if (commentText != null) {
                    FashPost post = posts.findById(id).get();
                    FashComment comment = new FashComment();
                    comment.setUser(loggedInUser);
                    comment.setText(commentText);
                    comment.setPost(post);
                    comment.setTitle(commentTitle);
                    comment.setDate(LocalDate.now());
                    comment.setTime(LocalTime.now());
                    comments.save(comment);
                    return "redirect:/postDetails/" + id;
                }
            }
            if (id == null) return "postDetails";

            //Kijken of de post bestaat en toevoegen aan model
            Optional<FashPost> optionalFashPost = posts.findById(id);
            if (optionalFashPost.isPresent()) {
                model.addAttribute("post", optionalFashPost.get());
                //De comments worden pas gezocht als de post bestaat
                ArrayList<FashComment> listComments=comments.findCommentsForPost(optionalFashPost.get());
                Collections.reverse(listComments);
                model.addAttribute("comments",listComments);
            }
            // om te kijken of de current user is de poster
            model.addAttribute("user", principal.getName());
            model.addAttribute("poster", posts.findById(id).get().getPoster().getUsername());
            return "postDetails";
        } catch (Exception e) {
            //NIKS
            return "postDetails";
        }
    }
    @GetMapping("/deletepost/{id}")
    public String deletePost(@PathVariable int id) {
        try {
            posts.deleteById(id);
        }catch (Exception e){
            //Niks doen aub
        }
        return "redirect:/profilepage";
    }
}
