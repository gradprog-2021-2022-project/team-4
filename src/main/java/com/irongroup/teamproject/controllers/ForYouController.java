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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class ForYouController {

    @Autowired
    PostRepository posts;
    @Autowired
    UserRepository users;
    @Autowired
    CommentRepository comments;

    @GetMapping({"/foryoupage", "/foryoupage/{id}"})
    public String foryoupage(Model model, @RequestParam(required = false) Integer postId, Principal principal, @RequestParam(required = false) String commentText
            ,@RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude) {
        if (principal != null) {
            FashUser loggedInUser = users.findFashUserByUsername(principal.getName());
            model.addAttribute("curUser", loggedInUser);
            model.addAttribute("loggedIn", true);
            if (commentText != null) {
                FashPost post = posts.findById(postId).get();
                FashComment comment=new FashComment();
                comment.setUser(loggedInUser);
                comment.setText(commentText);
                comment.setPost(post);
                comment.setDate(LocalDate.now());
                comment.setTime(LocalTime.now());
                comments.save(comment);
                return "redirect:/foryoupage";
            }
            if (longitude != null && latitude != null) {
                loggedInUser.setLatitude(latitude);
                loggedInUser.setLongitude(longitude);
                users.save(loggedInUser);
            }
        }
        //if (id == null) return "foryoupage";

        FashUser loggedUser = users.findFashUserByUsername(principal.getName());
        List<FashPost> postsFromFollowers = loggedUser.getPostsFromFollowing();
        Collections.sort(postsFromFollowers);
        Collections.reverse(postsFromFollowers);
        model.addAttribute("allposts",postsFromFollowers);
        /*andere manier om te sorteren
        Collections.sort(postsFromFollowers, new Comparator<FashPost>() {
            @Override
            public int compare(FashPost o1, FashPost o2) {
                return o1.id.compareTo(o2.id);
            }
        });*/

        /*oude code van Ibrahim die niet correct is
        //Posts van de users die je volgt ophalen
        Optional<FashUser> optionalFashUser = users.findById(id);
        Collection<FashUser> fashUsers = optionalFashUser.get().getFollowing();
        List<FashUser> listUsers =fashUsers.stream().toList();
        if(optionalFashUser.isPresent()) {
            model.addAttribute("fashUsers", fashUsers);
        }*/
        return "foryoupage";
    }

    @PutMapping("/foryoupage")
    public ResponseEntity<FashUser> updateUsers(Principal principal) {
        FashUser user = users.findFashUserByUsername(principal.getName());
        users.save(user);
        return ResponseEntity.ok(user);
    }
}
