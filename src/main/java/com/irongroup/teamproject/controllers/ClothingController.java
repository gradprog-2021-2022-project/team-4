package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.ClothingRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping({"/clothingdetail", "/clothingdetail/{id}"})
    public String clothingdetails(Model model, @PathVariable(required = false) Integer id, Principal principal) {
        try {
            if (id != null) {
                Clothing_Item item = clothingRepository.findById(id).get();
                model.addAttribute("item", item);
            }
        } catch (Exception e) {
            System.out.println("geen clothing item");
            return "redirect:/";
        }
        if(principal!=null){
            FashUser user=userRepository.findFashUserByUsername(principal.getName());
            model.addAttribute("fashuser",user);
        }
        return "clothing_detail";
    }
    @GetMapping("/saveItem/{id}")
    public String saveItem(@PathVariable Integer id){
        return "redirect:/clothingdetail/"+id;
    }
    @GetMapping("/removeItem/{id}")
    public String removeItem(@PathVariable Integer id){
        return "redirect:/clothingdetail/"+id;
    }
}
