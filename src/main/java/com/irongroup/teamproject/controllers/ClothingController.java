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
            //Op deze manier worden errors naar de home pagina gestuurd zodat de gebruiker geen error pagina krijgt
            System.out.println("geen clothing item");
            return "redirect:/";
        }
        if (principal != null) {
            FashUser user = userRepository.findFashUserByUsername(principal.getName());
            model.addAttribute("fashuser", user);
        }
        return "clothing_detail";
    }

    //Opslaan van een item in een gebruiker zijn lijst
    @GetMapping("/saveItem/{id}")
    public String saveItem(@PathVariable Integer id, Principal principal) {
        if (principal != null) {
            FashUser user = userRepository.findFashUserByUsername(principal.getName());
            if (clothingRepository.existsById(id)) {
                //Oproepen van een eigen methode voor makkelijk gebruik
                user.addItem(clothingRepository.findById(id).get());
                //Opslaan omdat anders de database zelf niet veranderd!!
                userRepository.save(user);
            }
        }
        return "redirect:/clothingdetail/" + id;
    }

    //Verwijderen van een item uit een gebruiker zijn lijst
    @GetMapping("/removeItem/{id}")
    public String removeItem(@PathVariable Integer id, Principal principal) {
        if(principal!=null){
            FashUser user=userRepository.findFashUserByUsername(principal.getName());
            if(clothingRepository.existsById(id)){
                //Oproepen van een eigen methode voor makkelijk gebruik
                user.removeItem(clothingRepository.findById(id).get());
                //Opslaan omdat anders de database zelf niet veranderd!!
                userRepository.save(user);
            }
        }
        return "redirect:/clothingdetail/" + id;
    }
}
