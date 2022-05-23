package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.repositories.ClothingRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EditPostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository users;
    @Autowired
    private ClothingRepository clothingRepository;

    @GetMapping("/editpost/{id}")
    public String postEdit() {
        return "editpost";
    }

    //post edit
    @ModelAttribute("valid")
    public FashPost findPost(@PathVariable(required = false) Integer id) {
        if (id!=null) {
            Optional<FashPost> optionalFashPost = postRepository.findById(id);
            if (optionalFashPost.isPresent()) return optionalFashPost.get();
        }
        return new FashPost();
    }

    @PostMapping("/editpost/{id}")
    public String postEditPost(Model model,@PathVariable int id, Principal principal,
                              @ModelAttribute("valid") @Valid FashPost valid, BindingResult bindingResult,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (bindingResult.hasErrors()) {
            return "/editpost";
        }
        postRepository.save(valid);
        //photo
        if(!multipartFile.getOriginalFilename().equals("")||multipartFile==null){
            valid.setPostPic(multipartFile.getInputStream().readAllBytes());
        }

        List<Clothing_Item> clothing_items = new ArrayList<>();
        //clothing_items = valid.getClothes().stream().toList();
        postRepository.save(valid);

        for (Clothing_Item c: valid.getClothes()) {
            if(c.getNaam()!=null && !c.getNaam().equals("")){
                clothing_items.add(c);
                clothingRepository.save(c);
            }
        }
        postRepository.save(valid);
        return "redirect:/postDetails/"+ id;
    }
}
