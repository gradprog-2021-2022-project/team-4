package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.ClothingRepository;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping({"/adminpagina"})
    public String adminpagina(Model model, @RequestParam(required = false) Integer id, @RequestParam(required = false) Integer postallow, @RequestParam(required = false) String username) {

        try {
            if(postallow!=null){
                //Code voor het aanpassen van user allowance
                FashUser user = users.findById(id).get();
                user.setPost_allowance(postallow);
                users.save(user);
                return "redirect:/adminpagina";
            }
            //Als de filters aan staan
            if (username != null && username.length() > 0) {
                model.addAttribute("filtered", true);
                //Voeg enkel items toe van de bepaalde gebruiker
                FashUser user = users.findFashUserByUsername(username);
                model.addAttribute("namefound",username);
                model.addAttribute("fashposters", user);
                model.addAttribute("fashclothes", clothing.findClothingOfUser(user.getId()));
                model.addAttribute("fashposts", posts.findbyUserId(user.getId()));
                model.addAttribute("comments", comments.findFashCommentByUserID(user.getId()));
                return "adminpage";
            } else {
                //Geen filter aan
                model.addAttribute("fashposters", users.findAll());
                model.addAttribute("fashclothes", clothing.findAll());
                model.addAttribute("fashposts", posts.findAll());
                model.addAttribute("comments", comments.findAll());
                model.addAttribute("filtered", false);
            }
        } catch (
                Exception e) {
            //NOG NIKS
        }
        return "adminpage";
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        try {
            users.delete(users.findById(userId).get());
        } catch (Exception e) {
            //NOG NIKS
        }
        return "redirect:/adminpagina";
    }

    @GetMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable Integer postId) {
        try {
            posts.delete(posts.findById(postId).get());
        } catch (Exception e) {
            //NOG NIKS
        }
        return "redirect:/adminpagina";
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(@PathVariable Integer itemId) {
        try {
            clothing.delete(clothing.findById(itemId).get());
        } catch (Exception e) {
            //NOG NIKS
        }
        return "redirect:/adminpagina";
    }

    @GetMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable Integer commentId) {
        try {
            comments.delete(comments.findById(commentId).get());
        } catch (Exception e) {
            //NOG NIKS
        }
        return "redirect:/adminpagina";
    }
}
