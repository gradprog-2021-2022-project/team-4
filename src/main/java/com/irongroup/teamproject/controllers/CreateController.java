package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.repositories.ClothingRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CreateController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository users;
    @Autowired
    private ClothingRepository clothingRepository;


    @Autowired
    List<String> nameList;

    @GetMapping("/postnew")
    public String postNew(Model model) {
        model.addAttribute("styles", nameList);
        return "createpost";
    }

    //post create
    @ModelAttribute("valid")
    public FashPost findPost(@PathVariable(required = false) Integer id) {
        if (id!=null) {
            Optional<FashPost> optionalFashPost = postRepository.findById(id);
            if (optionalFashPost.isPresent()) return optionalFashPost.get();
        }
        return new FashPost();
    }

    @PostMapping("/postnew")
    public String postNewPost(Model model, Principal principal,
                              @ModelAttribute("valid") @Valid FashPost valid, BindingResult bindingResult,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (bindingResult.hasErrors()) {
            return "createpost";
        }
        FashPost post = new FashPost();
        //photo
        if(!multipartFile.getOriginalFilename().equals("")||multipartFile==null){
            post.setPostPic(multipartFile.getInputStream().readAllBytes());
        }

        List<Clothing_Item> clothing_items = new ArrayList<>();
        postRepository.save(post);

        for (Clothing_Item c: valid.getClothes()) {
            if(c.getNaam()!=null && !c.getNaam().equals("")){
                c.setUserOwner(users.findFashUserByUsername(principal.getName()));
                c.setPost(post);
                clothing_items.add(c);
                clothingRepository.save(c);
            }
        }
        post.setLikes(0);
        post.setStijl(valid.getStijl());
        post.setClothes(clothing_items);
        post.setLocation(valid.getLocation());
        post.setDate(java.time.LocalDate.now());
        post.setTime(java.time.LocalTime.now());
        post.setPoster(users.findFashUserByUsername(principal.getName()));
        post.setText(valid.getText());
        postRepository.save(post);
        return "redirect:/postDetails/"+post.getId();
    }
}