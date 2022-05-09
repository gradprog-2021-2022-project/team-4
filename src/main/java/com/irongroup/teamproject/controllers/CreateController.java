package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
public class CreateController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository users;

    @GetMapping("/postnew")
    public String postNew(Model model) {
        return "createpost";
    }

    @ModelAttribute("post")
    public FashPost findPost(@PathVariable(required = false) Integer id) {
        if (id!=null) {
            Optional<FashPost> optionalFashPost = postRepository.findById(id);
            if (optionalFashPost.isPresent()) return optionalFashPost.get();
        }
        return new FashPost();
    }

    @PostMapping("/postnew")
    public String postNewPost(Model model, Principal principal,
                              @ModelAttribute("post") @Valid FashPost valid, BindingResult bindingResult,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(principal!= null) {
            model.addAttribute("loggedIn", true);
        }
        if (bindingResult.hasErrors()) {
            return "createpost";
        }
        FashPost post = new FashPost();

        if(!multipartFile.getOriginalFilename().equals("")||multipartFile==null){
            post.setInputstream(multipartFile.getInputStream().readAllBytes());
        }

        post.setDate(java.time.LocalDate.now());
        post.setTime(java.time.LocalTime.now());
        post.setPoster(users.findFashUserByUsername(principal.getName()));
        post.setText(valid.getText());
        post.setLocation(valid.getLocation());
        postRepository.save(post);
        return "redirect:/postDetails/"+post.getId();
    }

}
