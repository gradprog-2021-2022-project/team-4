package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
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
public class CreateController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository users;
    @Autowired
    private ClothingRepository clothingRepository;


    @Autowired
    List<String> nameList;

    @Autowired
    List<String> typeList;

    @GetMapping("/postnew")
    public String postNew(Model model, Principal principal) {
        FashUser  user = users.findFashUserByUsername(principal.getName());
        model.addAttribute("styles", nameList);
        model.addAttribute("types", typeList);
        model.addAttribute("user", user);
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
                              @RequestParam("image") MultipartFile multipartFile, @RequestParam(required = false) Double longitude,
                              @RequestParam(required = false) Double latitude) throws IOException {

        if (bindingResult.hasErrors()) {
            return "createpost";
        }
        FashPost post = new FashPost();
        //photo
        if(multipartFile!=null&&!multipartFile.getOriginalFilename().equals("")){
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
        FashUser user = users.findFashUserByUsername(principal.getName());
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        users.save(user);
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