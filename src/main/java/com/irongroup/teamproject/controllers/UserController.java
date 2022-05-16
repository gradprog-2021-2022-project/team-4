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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserRepository users;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/profilepage", "/profilepage/{id}" })
    public String profilepage(Model model, @PathVariable(required = false)Integer id, Principal principal){
        //Dit is als je naar je eigen profiel gaat
        if(id==null) {
            //als er niemand is ingelogd en je zoekt zelf de profilepage
            if (principal==null) {
                return "profilepage";
            }
            else {
                FashUser user = users.findFashUserByUsername(principal.getName());
                model.addAttribute("user", user);
                //Voor matthew, dit is de naam voor mensen die ge al volgt voor in html te gebruiken
                model.addAttribute("following",user.getFollowing());
            /*//Tijdelijke code voor testing
            for (FashUser u : user.getFollowers()
                 ) {
                System.out.println(u.username);
            }*/
                return "profilepage";
            }
        }
        //Dit is als een gebruiker wordt gezocht
        else {
            Optional<FashUser> optionalFashUser = users.findById(id);
            if(optionalFashUser.isPresent()){
                FashUser userpage = users.findFashUserByUsername(optionalFashUser.get().getUsername());
                FashUser userloggedin = users.findFashUserByUsername(principal.getName());
                //Kijken of ge al volgt en zoniet, volgen
                if (!userpage.followers.contains(userloggedin)){
                    //follow button veranderen naar follow
                    model.addAttribute("follow", "follow");
                }
                //Als ge wel volgt, unfollow doen
                else {
                    //follow button veranderen naar unfollow
                    model.addAttribute("follow", "unfollow");
                }
                model.addAttribute("user", optionalFashUser.get());
            }
            return "profilepage";
        }
    }

    @GetMapping({"/follow/{id}" })
    public String follow(Principal principal, @PathVariable Integer id){
        //Kijken of ge wel bent ingelogd
        if(principal!=null){
            //Kijken of de id die meegegeven is bestaat
            if(users.findById(id).get()!=null){
                FashUser userloggedin = users.findFashUserByUsername(principal.getName());
                FashUser userpage = users.findById(id).get();
                //Kijken of ge al volgt en zoniet, volgen
                if(!userloggedin.following.contains(userpage)){
                    userloggedin.follow(users.findById(id).get());
                    userpage.addFollower(userloggedin);
                }
                //Als ge wel volgt, unfollow doen
                else {
                    userloggedin.unFollow(users.findById(id).get());
                    users.findById(id).get().removeFollower(userloggedin);
                }
                //Natuurlijk opslaan zoals altijd
                users.save(userloggedin);
                users.save(userpage);
            }
        }
        return "redirect:/profilepage/" + id;
    }

    @GetMapping({"/followerspage/{id}"})
    public String followerspage(Model model, @PathVariable Integer id, @RequestParam(required = false) String keyword){
        logger.info("followerspage -- keyword=" + keyword);
        Optional<FashUser> optionalFashUser = users.findById(id);
        if(optionalFashUser.isPresent()){
            //keyword is het woord dat gezocht wordt via search bar
            model.addAttribute("keyword", keyword);
            model.addAttribute("user", optionalFashUser.get());
            //kijken of er een naam gezocht wordt
            if (keyword != null) {
                //zoekt alle users met het keyword in
                Collection<FashUser> FashUsers = users.findByKeyword(keyword);
                //maakt 2 lijsten aan die worden meegegeven
                Collection<FashUser> followingsearch = new ArrayList<>();
                Collection<FashUser> followerssearch = new ArrayList<>();

                // stream gebruiken om te filteren in een lijst
                /*List<FashUser> followerssearch = FashUsers.stream().filter(fu -> fu.getFollowing().contains(optionalFashUser.get())).collect(Collectors.toList());
                List<FashUser> followingsearch = FashUsers.stream().filter(fu -> fu.getFollowers().contains(optionalFashUser.get())).collect(Collectors.toList());*/

                //kijkt bij elke persoon die gevonden is of die in de following of followers lijst zit van de persoon van deze pagina
                for (FashUser user:FashUsers) {
                    if (optionalFashUser.get().getFollowing().contains(user)) {
                        followingsearch.add(user);
                    }
                    if (optionalFashUser.get().getFollowers().contains(user)) {
                        followerssearch.add(user);
                    }
                }
                model.addAttribute("following",followingsearch);
                model.addAttribute("followers",followerssearch);
                model.addAttribute("nrfollowing", ((Collection<?>) followingsearch).size());
                model.addAttribute("nrfollowers", ((Collection<?>) followerssearch).size());
            }
            else {
                //Voor matthew, dit is de naam voor mensen die ge al volgt voor in html te gebruiken
                model.addAttribute("following",optionalFashUser.get().getFollowing());
                model.addAttribute("followers",optionalFashUser.get().getFollowers());
                model.addAttribute("nrfollowing", optionalFashUser.get().aantalFollowing());
                model.addAttribute("nrfollowers", optionalFashUser.get().aantalFollowers());
            }
        }
        return "followerspage";
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

    @GetMapping({"/u/image","/u/image/{id}"})
    public void image(
                      HttpServletResponse response,
                      Principal principal,@PathVariable(required = false)Integer id) throws IOException {
        response.setContentType("image/jpg");

        if(id==null){
            Optional<FashUser> fashUserOptional = userRepository.findUserOptional(principal.getName());
            if (fashUserOptional.isPresent()&&fashUserOptional.get().getProfilePic()!=null){
                InputStream is = new ByteArrayInputStream(fashUserOptional.get().getProfilePic());
                IOUtils.copy(is, response.getOutputStream());
            }
        }
        else{
            Optional<FashUser> fashUserOptional = userRepository.findById(id);
            if (fashUserOptional.isPresent()&&fashUserOptional.get().getProfilePic()!=null){
                InputStream is = new ByteArrayInputStream(fashUserOptional.get().getProfilePic());
                IOUtils.copy(is, response.getOutputStream());
            }
        }
    }
}