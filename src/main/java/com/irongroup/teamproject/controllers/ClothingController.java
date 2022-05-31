package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Clothing_Item;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.ClothingRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

@Controller
public class ClothingController {
    @Autowired
    PostRepository posts;
    @Autowired
    ClothingRepository clothingRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/clothing/{id}")
    public String clothing(Model model, @PathVariable Integer id, @RequestParam(required = false) String kledingname, @RequestParam(required = false) Boolean date, Principal p) {
        try {
            FashUser loggedIn=userRepository.findFashUserByUsername(p.getName());
            FashUser userChosen=userRepository.findById(id).get();
            ArrayList<Clothing_Item> clothes=(ArrayList<Clothing_Item>) clothingRepository.findClothingOfUser(id);

            model.addAttribute("user",userChosen);

            //Je eigen kleding kunnen bekijken!
            if(loggedIn!=null && loggedIn.getId()==userChosen.id){
                model.addAttribute("user",loggedIn);
                clothes= (ArrayList<Clothing_Item>) clothingRepository.findSavedClothing(loggedIn.id);
                model.addAttribute("ownClothes",true);
            }
            //Als de date op yes staat, sorteren op datum
            if(date!=null){
                Collections.sort(clothes);
                Collections.reverse(clothes);
                model.addAttribute("date",date);
            }
            //Filteren op naam
            if(kledingname!=null && kledingname.length()>0){
                model.addAttribute("naampje",kledingname);
                clothes=sorted(clothes,kledingname);
            }
            model.addAttribute("clothes", clothes);
        } catch (Exception e) {
            //NOG NIKS
        }
        return "clothing_overview";
    }

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
        if (principal != null) {
            FashUser user = userRepository.findFashUserByUsername(principal.getName());
            if (clothingRepository.existsById(id)) {
                //Oproepen van een eigen methode voor makkelijk gebruik
                user.removeItem(clothingRepository.findById(id).get());
                //Opslaan omdat anders de database zelf niet veranderd!!
                userRepository.save(user);
            }
        }
        return "redirect:/clothingdetail/" + id;
    }

    //Kopen van een item, dit is een voorlopige oplossing
    @GetMapping("/buyItem/{id}")
    public String buyItem(@PathVariable Integer id) {
        try {
            Clothing_Item item = clothingRepository.findById(id).get();
            FashUser user = userRepository.findFashUserByUsername(item.getUserOwner().getUsername());
            //Aanpassen en altijd opslaan zodat de database aangepast kan worden.
            user.setPost_allowance(user.getPost_allowance() + 1);
            userRepository.save(user);
            //Redirect zonder een / kan gebruikt worden voor een externe website
            return "redirect:" + item.getLinkShop();
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    //Sorteer functie zodat beide lists kunnen gesorteerd worden!
    private ArrayList<Clothing_Item> sorted(ArrayList<Clothing_Item> clothes,String naam){
        for (Clothing_Item c:clothes
             ) {
            if (!c.getNaam().toLowerCase().contains(naam.toLowerCase())){
                clothes.remove(c);
            }
        }
        return clothes;
    }
}
