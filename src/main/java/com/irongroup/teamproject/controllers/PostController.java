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

import javax.xml.stream.events.Comment;
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
    public String explorepage(Model model, Principal principal,@RequestParam(required = false) Boolean closeby, @RequestParam(required = false)Integer id, @RequestParam(required = false) String commentText,@RequestParam(required = false) String commentTitle){
        final String loginName = principal==null ? "NOBODY" : principal.getName();

        System.out.println(loginName);
        Collection<FashPost> postsmade=posts.findAll();
        model.addAttribute("fashposts",postsmade);
        Collection<FashUser> fashUsers=users.findUsersWithPosts();
        if(principal!= null){
            FashUser loggedInUser=users.findFashUserByUsername(principal.getName());
            model.addAttribute("loggedIn",true);
            if(commentText!=null){
                FashPost post=posts.findById(id).get();
                comments.save(new FashComment(Math.toIntExact(comments.count())+1,loggedInUser,post,commentTitle,commentText, LocalDate.now(), LocalTime.now()));
                return "redirect:/explorepage";
            }
        }
        System.out.println("closeby = "+closeby);
        if (closeby!=null && closeby &&principal!=null){
            model.addAttribute("fashUsers", orderByLocation(principal));
        }
        else{
            model.addAttribute("fashUsers",fashUsers);
        }
        model.addAttribute("closeby", closeby);
        return "explorepage";
    }

    public ArrayList<FashUser> orderByLocation(Principal principal){

        FashUser user = users.findFashUserByUsername(principal.getName());

        ArrayList<FashUser> closest = new ArrayList<>();

        for (FashUser u: users.findUsersWithPosts()) {
            if(u.getLongitude()!=null&&u.getLatitude()!=null&&user.getLongitude()!=null&&user.getLatitude()!=null){
                System.out.println(haversine(user.getLatitude(),user.getLongitude(),u.getLatitude(),u.getLongitude()));
                Double distanceInKm = haversine(user.getLatitude(),user.getLongitude(),u.getLatitude(),u.getLongitude());

                if (distanceInKm<5){
                    closest.add(u);
                }
            }
        }

        return closest;
    }

    public Double haversine(Double lat1, Double lon1, Double lat2, Double lon2){
        //Afstand tussen de lengtegraad en breedtegraad berekenen
        double dLat = (lat2 - lat1) * Math.PI / 180.0;
        double dLon = (lon2 -lon1)* Math.PI / 180.0;

        //omvormen naar radialen
        lat1 = (lat1) * Math.PI /180.0;
        lat2 = (lat2)* Math.PI /180.0;

        Double a = Math.pow(Math.sin(dLat /2),2) + Math.pow(Math.sin(dLon/2),2) * Math.cos(lat1) * Math.cos(lat2);
        Double rad = 6371.0;
        Double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
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
}