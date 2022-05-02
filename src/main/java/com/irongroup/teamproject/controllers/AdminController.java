package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.model.FashComment;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.ClothingRepository;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

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
    public String adminpage(Model model) {

        /*
        //alles leegmaken (enkel voor testjes)
        clothing.deleteAll();
        comments.deleteAll();
        posts.deleteAll();
        users.deleteAll();

        FashUser Allee = new FashUser(1, "Allee", "Elias", "Neel", 5, null, null, null, null);
        users.save(Allee);
        FashPost post = new FashPost(1, null, Allee, null, LocalDate.now(), LocalTime.now(), "Antwerpen");
        posts.save(post);
        Clothing_Item broekje = new Clothing_Item(1, "Broek", "Geeltje", 29.99, "nergens...", post, null, Allee);
        clothing.save(broekje);
        Collection<Clothing_Item> clothes= clothing.findAll();
        post.setClothes(clothes);
        posts.save(post);
        FashComment comment= new FashComment(1,Allee,post,"Neen","dit is een tekstje",LocalDate.now(),LocalTime.now());
        comments.save(comment);*/

        model.addAttribute("fashposters", users.findAll());
        model.addAttribute("fashclothes", clothing.findAll());
        model.addAttribute("fashposts", posts.findAll());
        model.addAttribute("comments",comments.findAll());

        return "adminpage";
    }
}