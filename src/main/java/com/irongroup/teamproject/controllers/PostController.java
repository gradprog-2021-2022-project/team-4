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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    PostRepository posts;
    @Autowired
    UserRepository users;
    @Autowired
    CommentRepository comments;

    @GetMapping({"/explorepage","/"})
    public String explorepage(Model model, Principal principal){
        final String loginName = principal==null ? "NOBODY" : principal.getName();
        System.out.println(loginName);
        Collection<FashPost> postsmade=posts.findAll();
        model.addAttribute("fashposts",postsmade);
        Collection<FashUser> fashUsers=users.findUsersWithPosts();
        model.addAttribute("fashUsers",fashUsers);
        return "explorepage";
    }
    @GetMapping({"/foryoupage"})
    public String foryoupage(){
        return "foryoupage";
    }
    @GetMapping({"/postDetails/{id}","/postDetails"})
    public String postDetails(Model model, @PathVariable(required = false)Integer id, Principal principal, @RequestParam(required = false) String commentText,@RequestParam(required = false) String commentTitle){
        //Kijken of je aangemeld bent;
        //boolean aangemeld= principal != null;

        //Als de gebruiker niet aangemeld is, kan je geen comments plaatsen
        if(principal!= null){
            FashUser loggedInUser=users.findFashUserByUsername(principal.getName());
            model.addAttribute("loggedIn",true);
            if(commentText!=null){
                FashPost post=posts.findById(id).get();
                comments.save(new FashComment(Math.toIntExact(comments.count())+1,loggedInUser,post,commentTitle,commentText, LocalDate.now(), LocalTime.now()));
                //return "redirect:/postDetails/"+id;
            }
        }
        if(id==null) return "postDetails";

        //Kijken of de post bestaat en toevoegen aan model
        Optional<FashPost> optionalFashPost = posts.findById(id);
        if(optionalFashPost.isPresent()){
            model.addAttribute("post", optionalFashPost.get());
            //De comments worden pas gezocht als de post bestaat
            model.addAttribute("comments",comments.findCommentsForPost(optionalFashPost.get()));
        }
        return "postDetails";
    }
    //Liken stuurt gewoon terug naar dezelfde pagina met toegevoegde like
    @GetMapping({"/likePost/{id}"})
    public String likePost(Model model,@PathVariable Integer id){
        FashPost post=posts.findById(id).get();
        post.addLike();
        posts.save(post);
        return "redirect:/postDetails/"+id;
    }
}
