package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashComment;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public String explorepage(Model model, Principal principal, @RequestParam(required = false)Integer id, @RequestParam(required = false) String commentText,@RequestParam(required = false) String commentTitle){
        final String loginName = principal==null ? "NOBODY" : principal.getName();
        System.out.println(loginName);
        Collection<FashPost> postsmade=posts.findAll();
        model.addAttribute("fashposts",postsmade);
        Collection<FashUser> fashUsers=users.findUsersWithPosts();
        model.addAttribute("fashUsers",fashUsers);
        if(principal!= null){
            FashUser loggedInUser=users.findFashUserByUsername(principal.getName());
            model.addAttribute("loggedIn",true);
            if(commentText!=null){
                FashPost post=posts.findById(id).get();
                comments.save(new FashComment(Math.toIntExact(comments.count())+1,loggedInUser,post,commentTitle,commentText, LocalDate.now(), LocalTime.now()));
                return "redirect:/explorepage";
            }
        }
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
                return "redirect:/postDetails/"+id;
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
    //Laden van een foto van post, gebaseerd op Brent zijn profielfotof
    @GetMapping("/p/image/{id}")
    public void image(
            HttpServletResponse response,
            @PathVariable Integer id) throws IOException {
        response.setContentType("image/jpg");

        Optional<FashPost> postopt=posts.findById(id);
        if (postopt.isPresent()&&postopt.get().getPostPic()!=null){
            InputStream is = new ByteArrayInputStream(postopt.get().getPostPic());
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}