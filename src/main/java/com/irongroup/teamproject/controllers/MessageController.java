package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Conversation;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.model.Message;
import com.irongroup.teamproject.repositories.ConversationRepository;
import com.irongroup.teamproject.repositories.MessageRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.jmx.JmxEndpointsSupplier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class MessageController {
    @Autowired
    UserRepository users;
    @Autowired
    ConversationRepository convos;
    @Autowired
    MessageRepository allMessages;

    // TODO : checks toevoegen aub eliasje
    @GetMapping("/messages")
    public String messagelist(Principal p, Model model) {
        //In een try catch= geen ifke
        try {
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            model.addAttribute("followers", loggedIn.followers);
            model.addAttribute("convos", loggedIn.getConversations());
            return "user/messagelist";
        } catch (Exception e) {
            return "redirect:/explorepage";
        }
    }

    // DONE : users worden verwijderd na sturen message (gefixt)
    // done : voeg een check toe om te zien of de gebruiker toegang heeft tot de convo!!
    @GetMapping({"/messages/{id}"})
    public String conversation(Principal p, Model model, @PathVariable Integer id, @RequestParam(required = false) String text) {
        //Als er geen bericht gestuurd word
        if (text == null || text.length() < 1) {
            //In een try catch= geen ifke
            try {
                //Gebruiker vinden die ingelogd is
                FashUser loggedIn = users.findFashUserByUsername(p.getName());
                //Eerst kijken of de conversatie al bestaat
                //Convo vinden die gevraagd word
                Conversation conversation = convos.findbyID(id);
                if (conversation.getUsers().contains(loggedIn)) {
                    //Toevoegen aan het model enkel als de gebruiker toegang heeft tot de convo
                    model.addAttribute("convo", conversation);
                    model.addAttribute("loggedUser", loggedIn);
                    return "user/conversation";
                } else {
                    System.out.println("we zitten hier!");
                    return "redirect:/explorepage";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/explorepage";
            }
        } else {
            try {
                //Zoek de convo die een bericht moet ontvangen
                Conversation convo = convos.findbyID(id);
                //De verzender is altijd de persoon die aangemeld is aangezien hij degene is die deze actie aanroept
                FashUser sender = users.findFashUserByUsername(p.getName());
                //De ontvangers zoeken en de verstuurder eruit halen, anders ziet hij het bericht twee keer
                Collection<FashUser> receivers = convo.getUsers();
                receivers.remove(sender);
                //Alle berichten opvragen van de convo
                Collection<Message> messages = convo.getMessages();
                //Het bericht aanmaken en opslaan in de message tabel
                Message message = new Message(Math.toIntExact(allMessages.count()) + 1, convo, sender, receivers, text);
                allMessages.save(message);
                messages.add(message);
                //Belangrijk, de messages setten en ook opslaan in de database;
                convo.setMessages(messages);
                //Voor opslaan, de gebruiker er terug inzetten, anders kan hij de conversatie ni meer in!
                receivers.add(sender);
                convo.setUsers(receivers);
                convos.save(convo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:/messages/" + id;
        }
    }

    // DONE : users worden niet goed toegevoegd aan gesprek (hier zit niet de fout)
    // DONE : BUG FIXEN
    @GetMapping("/checkconvo/{id}")
    public String checkConvo(@PathVariable Integer id, Principal p) {
        String dingetje = "/explorepage";
        try {
            //De twee gebruikers zoeken
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            FashUser newUser = users.findById(id).get();

            /*//Tijdelijke code voor testing
            if(newUser==null) System.out.println("Gene user!");*/

            //Als de gebruiker nog geen convo heeft met de gebruiker
            if (!loggedIn.hasConvoWithUser(newUser)) {
                Conversation c = new Conversation();
                //int idke=c.getId();
                //c.setId(idke);
                ArrayList<FashUser> usersconvo=new ArrayList<>();
                c.setConvoNaam(newUser.getUsername());

                usersconvo.add(loggedIn);
                usersconvo.add(newUser);
                c.setUsers(usersconvo);
                /*
                c.addUser(loggedIn);
                c.addUser(newUser);
                */
                loggedIn.addConvo(c);
                newUser.addConvo(c);//Dingen opslaan !!!
                convos.save(c);
                users.save(loggedIn);
                users.save(newUser);
                dingetje = "redirect:/messages/" + c.getId();
                //dingetje= "redirect:/nergens";
            } else {
                dingetje = "redirect:/messages/" + loggedIn.conversationWith(newUser).getId();
            }
        } catch (Exception e) {
            //Nogniks
            e.printStackTrace();
            return "/profilepage";
        }
        return dingetje;
    }

    // DONE : ZORGEN VOOR EEN NAAM (werkt)
    @GetMapping("/adduser/{convoID}")
    public String newConvo(Principal p,@RequestParam Integer id,@PathVariable Integer convoID) {
        try {
            Conversation currentc=convos.findbyID(convoID);

            //Als een convo al meer dan 2 users heeft, geen nieuwe maken maar gebruiker toevoegen aan huidige
            if(currentc.getUsers().size()>2){
                currentc.addUser(users.findById(id).get());
                currentc.setConvoNaam(currentc.getConvoNaam()+users.findById(id).get().getUsername()+",");
                users.findById(id).get().addConvo(currentc);
                convos.save(currentc);

                return "redirect:/messages/"+currentc.getId();
            }else{
                //Anders wel een nieuwe convo maken
                Conversation c = new Conversation();
                String naam= "";

                //De gebruikers van de huidige convo toevoegen aan de nieuwe convo
                for (FashUser u: currentc.getUsers()
                ) {
                    c.addUser(u);
                    u.addConvo(c);
                    naam = naam + u.username+",";
                }
                //De nieuwe user toevoegen
                c.addUser(users.findById(id).get());
                //Aan de nieuwe user de convo toevoegen
                users.findById(id).get().addConvo(c);
                c.setConvoNaam(naam);
                //Dingen opslaan !!!
                convos.save(c);
                return "redirect:/messages/"+c.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/messages";
        }
    }

    /*
    @GetMapping("/sendmessage/{id}")
    public String sendMessage(@PathVariable Integer id, @RequestParam String text, Principal p) {
    }*/
}
