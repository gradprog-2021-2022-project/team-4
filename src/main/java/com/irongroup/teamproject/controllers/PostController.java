package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.FashComment;
import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.repositories.CommentRepository;
import com.irongroup.teamproject.repositories.PostRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.stream.events.Comment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class PostController {
    @Autowired
    PostRepository posts;
    @Autowired
    UserRepository users;
    @Autowired
    CommentRepository comments;

    //De gewone / redirecten naar explorepage zodat de gebruiker nooit een lege URL kan zien
    @GetMapping("/")
    public String redirectje() {
        return "redirect:/explorepage";
    }


    //FILTERS WERKEN!!!
    @GetMapping("/explorepage")
    public String explorepage(Model model, Principal principal, @RequestParam(required = false) Boolean closeby, @RequestParam(required = false) Boolean showFilter
            , @RequestParam(required = false) Integer id, @RequestParam(required = false) String commentText, @RequestParam(required = false) String commentTitle,
                              @RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude, @RequestParam(required = false) String style,@RequestParam(required = false) Double minPrijs,@RequestParam(required = false) Double maxPrijs) {
        final String loginName = principal == null ? "NOBODY" : principal.getName();
        /*
        Op de explore page roep je eerst alle posts aan en dan worden de filters in volgorde aangezet, zo moet niks gecombineerd worden!
        */
        //Eerst alle posts ophalen!
        Collection<FashUser> fashUsers = users.findUsersWithPosts();
        //Kijken voor de stijl en zoja filteren
        if (style != null && style.length() > 1) {
            System.out.println("met stijl");
            fashUsers=filterPosts(fashUsers,style);
        }
        if(minPrijs!=null || maxPrijs!=null){
            fashUsers=filterPrice(fashUsers,minPrijs,maxPrijs);
        }

        System.out.println(loginName);
        Collection<FashPost> postsmade = posts.findAll();
        model.addAttribute("fashposts", postsmade);
        if (principal != null) {
            FashUser loggedInUser = users.findFashUserByUsername(principal.getName());
            model.addAttribute("curUser", loggedInUser);
            model.addAttribute("loggedIn", true);
            if (commentText != null) {
                FashPost post = posts.findById(id).get();
                comments.save(new FashComment(Math.toIntExact(comments.count()) + 1, loggedInUser, post, commentTitle, commentText, LocalDate.now(), LocalTime.now()));
                return "redirect:/explorepage";
            }
            if (longitude != null && latitude != null) {
                loggedInUser.setLatitude(latitude);
                loggedInUser.setLongitude(longitude);
                users.save(loggedInUser);
            }
        }
        System.out.println("closeby = " + closeby);
        if (closeby != null && closeby && principal != null) {
            model.addAttribute("fashUsers", orderByLocation(principal, fashUsers));
        } else {
            model.addAttribute("fashUsers", fashUsers);
        }
        model.addAttribute("longitude", longitude);
        model.addAttribute("latitude", latitude);
        model.addAttribute("closeby", closeby);
        model.addAttribute("showFilter", showFilter);
        return "explorepage";
    }


    @PutMapping("/explorepage")
    public ResponseEntity<FashUser> updateUser(Principal principal) {
        FashUser user = users.findFashUserByUsername(principal.getName());
        users.save(user);
        return ResponseEntity.ok(user);
    }

    //Filteren op prijs
    private ArrayList<FashUser> filterPrice(Collection<FashUser> fashUsers,Double minPrijs, Double maxPrijs){
        if(minPrijs==null) {minPrijs=Double.POSITIVE_INFINITY;}
        ArrayList<FashUser> users=new ArrayList<>();
        for (FashUser f:fashUsers
        ) {
            if(f.getLastPost().getTotalPrice()>minPrijs && f.getLastPost().getTotalPrice()<maxPrijs){
                users.add(f);
            }
        }return users;
    }

    //Filteren op stijl
    private ArrayList<FashUser> filterPosts(Collection<FashUser> fashUsers, String stijl){
        ArrayList<FashUser> users=new ArrayList<>();
        for (FashUser f:fashUsers
             ) {
            if(f.getLastPost().getStijl().equalsIgnoreCase(stijl)){
                users.add(f);
            }
        }return users;
    }

    //Lijst die gebasseerd is op locatie filteren op 5km afstand
    private ArrayList<FashUser> orderByLocation(Principal principal, Collection<FashUser> fashUsers) {
        FashUser user = users.findFashUserByUsername(principal.getName());

        ArrayList<FashUser> closest = new ArrayList<>();

        for (FashUser u : fashUsers) {
            if (u.getLongitude() != null && u.getLatitude() != null && user.getLongitude() != null && user.getLatitude() != null) {
                System.out.println(haversine(user.getLatitude(), user.getLongitude(), u.getLatitude(), u.getLongitude()));
                Double distanceInKm = haversine(user.getLatitude(), user.getLongitude(), u.getLatitude(), u.getLongitude());

                //Nu kan je ook filteren op stijl !
                if (distanceInKm < 5) {
                    closest.add(u);
                }
            }
        }
        return closest;
    }

    //Haversine formule om afstand tussen 2 coordinaten te berekennen
    public Double haversine(Double lat1, Double lon1, Double lat2, Double lon2) {
        //Afstand tussen de lengtegraad en breedtegraad berekenen
        double dLat = (lat2 - lat1) * Math.PI / 180.0;
        double dLon = (lon2 - lon1) * Math.PI / 180.0;

        //omvormen naar radialen
        lat1 = (lat1) * Math.PI / 180.0;
        lat2 = (lat2) * Math.PI / 180.0;

        Double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        Double rad = 6371.0;
        Double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    @GetMapping({"/foryoupage"})
    public String foryoupage() {
        return "foryoupage";
    }

    @GetMapping({"/postDetails/{id}", "/postDetails"})
    public String postDetails(Model model, @PathVariable(required = false) Integer id, Principal
            principal, @RequestParam(required = false) String commentText, @RequestParam(required = false) String
                                      commentTitle) {
        //Kijken of je aangemeld bent;
        //boolean aangemeld= principal != null;
        //Als de gebruiker niet aangemeld is, kan je geen comments plaatsen
        if (principal != null) {
            FashUser loggedInUser = users.findFashUserByUsername(principal.getName());
            if (commentText != null) {
                FashPost post = posts.findById(id).get();
                comments.save(new FashComment(Math.toIntExact(comments.count()) + 1, loggedInUser, post, commentTitle, commentText, LocalDate.now(), LocalTime.now()));
                return "redirect:/postDetails/" + id;
            }
        }
        if (id == null) return "postDetails";

        //Kijken of de post bestaat en toevoegen aan model
        Optional<FashPost> optionalFashPost = posts.findById(id);
        if (optionalFashPost.isPresent()) {
            model.addAttribute("post", optionalFashPost.get());
            //De comments worden pas gezocht als de post bestaat
            model.addAttribute("comments", comments.findCommentsForPost(optionalFashPost.get()));
        }
        return "postDetails";
    }

    //Liken stuurt gewoon terug naar dezelfde pagina met toegevoegde like
    @GetMapping({"/likePost/{id}"})
    public String likePost(Model model, @PathVariable Integer id) {
        FashPost post = posts.findById(id).get();
        post.addLike();
        posts.save(post);
        return "redirect:/postDetails/" + id;
    }

    //Laden van een foto van post, gebaseerd op Brent zijn profielfotof
    @GetMapping("/p/image/{id}")
    public void image(
            HttpServletResponse response,
            @PathVariable Integer id) throws IOException {
        response.setContentType("image/jpg");

        Optional<FashPost> postopt = posts.findById(id);
        if (postopt.isPresent() && postopt.get().getPostPic() != null) {
            InputStream is = new ByteArrayInputStream(postopt.get().getPostPic());
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}