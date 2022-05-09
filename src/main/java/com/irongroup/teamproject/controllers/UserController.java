package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    UserRepository users;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @GetMapping({"/profilepage", "/profilepage/{id}" })
    public String profilepage(Model model, @PathVariable(required = false)Integer id, Principal principal){
        //Dit is als een gebruiker wordt gezocht
        if(id==null) {
            FashUser user = users.findFashUserByUsername(principal.getName());
            model.addAttribute("user", user);
            //Voor matthew, dit is de naam voor mensen die ge al volgt voor in html te gebruiken
            model.addAttribute("following",user.getFollowers());
            /*//Tijdelijke code voor testing
            for (FashUser u : user.getFollowers()
                 ) {
                System.out.println(u.username);
            }*/
            return "profilepage";
        }
        //Dit is als je naar je eigen profiel gaat
        else {
            Optional<FashUser> optionalFashUser = users.findById(id);
            if(optionalFashUser.isPresent()){
                model.addAttribute("user", optionalFashUser.get());
                //Voor matthew, dit is de naam voor mensen die ge al volgt voor in html te gebruiken
                model.addAttribute("following",optionalFashUser.get().getFollowers());
            }
            return "profilepage";
        }
    }

    @GetMapping({"/follow/{id}" })
    public String follow(Model model, Principal principal, @PathVariable Integer id){
        //Kijken of ge wel bent ingelogd
        if(principal!=null){
            //Kijken of de id die meegegeven is bestaat
            if(users.findById(id).get()!=null){
                FashUser user = users.findFashUserByUsername(principal.getName());
                //Kijken of ge al volgt en zoniet, volgen
                if(!user.followers.contains(users.findById(id).get())){
                    user.follow(users.findById(id).get());
                }
                //Als ge wel volgt, unfollow doen
                else {user.unFollow(users.findById(id).get());}
                //Natuurlijk opslaan zoals altijd
                users.save(user);
            }
        }
        return "redirect:/profilepage/" + id;
    }

    @GetMapping({"/loginerror"})
    public String loginerror(){
        return "loginerror";
    }

    @GetMapping("/logout")
    public String logout(Principal principal, Model model) {
        if (principal == null) return "redirect:/";
        return "user/logout";
    }

    @GetMapping("/login")
    public String login(Principal principal){
        if(principal!=null) return "redirect:/";
        return "user/login";
    }

    //REGISTER
    @ModelAttribute("valid")
    public FashUser user(){
        return new FashUser();
    }

    @GetMapping("/register")
    public String register(Principal principal, Model model) {
        if (principal != null) return "redirect:/";
        return "user/register";
    }
    @PostMapping({"/register"})
    public String register(Model model, @ModelAttribute("valid") @Valid FashUser valid, BindingResult bindingResult,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {
        if(bindingResult.hasErrors()){
            return "user/register";
        }
        if(userRepository.findFashUserByUsername(valid.getUsername())!=null){
            ObjectError error = new ObjectError("nameDup","Name is already in use");
            model.addAttribute("error", error);
            return "user/register";
        }
        FashUser user = new FashUser();

        if(!multipartFile.getOriginalFilename().equals("")||multipartFile==null){
            user.setProfilePic(multipartFile.getInputStream().readAllBytes());
        }

        user.setFirst_name(valid.getFirst_name());
        user.setLast_name(valid.getLast_name());
        user.setPost_allowance(3);
        user.setUsername(valid.getUsername());
        user.setPassword(passwordEncoder.encode(valid.getPassword()));
        user.setRole("user");
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping({"/photodisplay" })
    public String photodisplay(Model model, Principal principal){
        if(principal==null) return "redirect:/";
        FashUser user = users.findFashUserByUsername(principal.getName());
        model.addAttribute("user", user);

        return "photodisplay";
    }

    @GetMapping("/u/image")
    public void image(
                      HttpServletResponse response,
                      Principal principal) throws IOException {
        response.setContentType("image/jpg");

        Optional<FashUser> fashUserOptional = userRepository.findUserOptional(principal.getName());
        if (fashUserOptional.isPresent()&&fashUserOptional.get().getProfilePic()!=null){
            InputStream is = new ByteArrayInputStream(fashUserOptional.get().getProfilePic());
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    @GetMapping("/postnew")
    public String postNew(Model model) {
        return "user/createpost";
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
                              @RequestParam("image")MultipartFile multipartFile) throws IOException {

        if (principal != null) {
            model.addAttribute("loggedIn", true);
        }
        if (bindingResult.hasErrors()) {
            return "user/createpost";
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
