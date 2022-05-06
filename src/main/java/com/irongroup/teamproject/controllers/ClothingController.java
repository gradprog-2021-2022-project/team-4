package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.repositories.ClothingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;

    @GetMapping({"/clothingdetail", "/clothingdetail/{id}"})
    public String clothingdetails(Model model, @PathVariable(required = false) Integer id) {
        try {
            if (id != null) {
                Clothing_Item item = clothingRepository.findById(id).get();
                model.addAttribute("item", item);
            }
        } catch (Exception e) {
            System.out.println("geen clothing item");
        }
        return "clothing_detail";
    }
}
