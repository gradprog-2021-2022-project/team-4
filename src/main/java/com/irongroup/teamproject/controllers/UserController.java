package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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

    @GetMapping({"/profilepage", "/profilepage/{id}" })
    public String profilepage(Model model, @PathVariable(required = false)Integer id, Principal principal){
        if(id==null) {
            FashUser user = users.findFashUserByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("following",user.getFollowers());
            /*//Tijdelijke code voor testing
            for (FashUser u : user.getFollowers()
                 ) {
                System.out.println(u.username);
            }*/
            return "profilepage";
        }
        else {
            Optional<FashUser> optionalFashUser = users.findById(id);
            if(optionalFashUser.isPresent()){
                model.addAttribute("user", optionalFashUser.get());
                model.addAttribute("following",optionalFashUser.get().getFollowers());
            }
            return "profilepage";
        }
    }

    @GetMapping({"/follow/{id}" })
    public String follow(Model model, Principal principal, @PathVariable Integer id){
        if(principal!=null){
            if(users.findById(id).get()!=null){
                FashUser user = users.findFashUserByUsername(principal.getName());
                if(!user.followers.contains(users.findById(id).get())){
                    user.follow(users.findById(id).get());
                }else {user.unFollow(users.findById(id).get());}
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
        String fileName = "";
        if(!multipartFile.getOriginalFilename().equals("")){
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            fileName = fileName.replace(" ","");
            user.setPhotos(fileName);
        }

        user.setFirst_name(valid.getFirst_name());
        user.setLast_name(valid.getLast_name());
        user.setPost_allowance(3);
        user.setUsername(valid.getUsername());
        user.setPassword(passwordEncoder.encode(valid.getPassword()));
        user.setRole("user");
        userRepository.save(user);
        String uploadDir = "src/main/resources/user-photos/" + user.getId();
        if(!multipartFile.getOriginalFilename().equals("")){
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/login";
    }
}
