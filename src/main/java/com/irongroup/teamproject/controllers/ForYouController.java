package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashComment;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class ForYouController {

    @Autowired
    PostRepository posts;
    @Autowired
    UserRepository users;
    @Autowired
    CommentRepository comments;
    @Autowired
    List<String> nameList;

    @GetMapping({"/foryoupage", "/foryoupage/{id}"})
    public String foryoupage(Model model, @RequestParam(required = false) Integer postId, Principal principal, @RequestParam(required = false) String commentText
            ,@RequestParam(required = false) String style, @RequestParam(required = false) Double minPrijs,@RequestParam(required = false) Double maxPrijs,@RequestParam(required = false) String naamPerson) {
        if (principal != null) {
            //model.addAttribute("loggedIn", true);
            FashUser loggedUser = users.findFashUserByUsername(principal.getName());
            List<FashPost> postsFromFollowers = loggedUser.getPostsFromFollowing();
            Collections.sort(postsFromFollowers);
            Collections.reverse(postsFromFollowers);
            model.addAttribute("curUser", loggedUser);

            model.addAttribute("styles", nameList);

            //Filters uitvoeren
            if (style != null && style.length() > 1) {
                System.out.println("met stijl");
                postsFromFollowers = filterPosts(postsFromFollowers, style);
                //Toevoegen aan model voor filter check
                model.addAttribute("stijlFilter",style);
            }
            if (minPrijs != null || maxPrijs != null) {
                postsFromFollowers= filterPrice(postsFromFollowers, minPrijs, maxPrijs);

                //Doorgeven van de filters aan het model
                model.addAttribute("minFilter",minPrijs);
                model.addAttribute("maxFilter",maxPrijs);
            }

            if(naamPerson!=null && naamPerson.length()>0){
                postsFromFollowers= filterName(postsFromFollowers,naamPerson);
                model.addAttribute("naamPerson", naamPerson);
            }

            //Toevoegen van gesorteerde en gefilterde posts aan het model
            model.addAttribute("allposts",postsFromFollowers);

            //Comment enkel toevoegen als die geplaatst kan worden
            if (commentText != null) {
                FashPost post = posts.findById(postId).get();
                FashComment comment=new FashComment();
                comment.setUser(loggedUser);
                comment.setText(commentText);
                comment.setPost(post);
                comment.setDate(LocalDate.now());
                comment.setTime(LocalTime.now());
                comments.save(comment);
                return "redirect:/foryoupage";
            }
        }
        return "foryoupage";
    }

    @PutMapping("/foryoupage")
    public ResponseEntity<FashUser> updateUsers(Principal principal) {
        FashUser user = users.findFashUserByUsername(principal.getName());
        users.save(user);
        return ResponseEntity.ok(user);
    }
    //Filteren op prijs
    private ArrayList<FashPost> filterPrice(Collection<FashPost> posts, Double minPrijs, Double maxPrijs) {
        if (maxPrijs == null) {
            maxPrijs = Double.POSITIVE_INFINITY;
        }
        if (minPrijs == null) {
            minPrijs = 0.0;
        }
        ArrayList<FashPost> filtered = new ArrayList<>();
        for (FashPost p:posts
        ) {
            if (p.getTotalPrice() > minPrijs && p.getTotalPrice() < maxPrijs) {
                filtered.add(p);
            }
        }
        return filtered;
    }
    //Filteren op stijl
    private ArrayList<FashPost> filterPosts(Collection<FashPost> posts, String stijl) {
        ArrayList<FashPost> filtered = new ArrayList<>();
        for (FashPost p : posts
        ) {
            if (p.getStijl()!=null && p.getStijl().equalsIgnoreCase(stijl)) {
                filtered.add(p);
            }
        }
        return filtered;
    }
    //Filteren op naam
    private ArrayList<FashPost> filterName(Collection<FashPost> posts, String naam) {
        ArrayList<FashPost> filtered = new ArrayList<>();
        for (FashPost p : posts
        ) {
            if (p.getPoster().getUsername()!=null && p.getPoster().getUsername().toLowerCase().contains(naam.toLowerCase())) {
                filtered.add(p);
            }
        }
        return filtered;
    }
}
