package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserRepository users;
    @GetMapping({"/profilepage/{id}", "profilepage"})
    public String profilepage(Model model, @PathVariable(required = false)Integer id){
        if(id==null) return "profilepage";
        Optional<FashUser> optionalFashUser = users.findById(id);
        if(optionalFashUser.isPresent()){
            model.addAttribute("user", optionalFashUser.get());
        }
        return "profilepage";
    }
}
