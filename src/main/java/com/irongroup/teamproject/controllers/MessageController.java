package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Conversation;
import com.irongroup.teamproject.model.FashUser;
import com.irongroup.teamproject.model.Message;
import com.irongroup.teamproject.repositories.ConversationRepository;
import com.irongroup.teamproject.repositories.MessageRepository;
import com.irongroup.teamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    UserRepository users;
    @Autowired
    ConversationRepository convos;
    @Autowired
    MessageRepository allMessages;

    @GetMapping("/messages")
    public String messagelist(Principal p, Model model) {
        try {
            //Kijken of de ingelogde user bestaat
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            //De volgers en volgende van de user toevoegen om nieuwe convos te starten
            model.addAttribute("followers", loggedIn.findBoth());
            //Alle convos opvragen van de user en daarna sorteren
            List<Conversation> convos= loggedIn.getConversations();
            Collections.sort(convos);
            //Collections.reverse(convos);
            model.addAttribute("convos", convos);
            model.addAttribute("loggedIn",loggedIn);
            return "user/messagelist";
        } catch (Exception e) {
            //e.printStackTrace();
            return "redirect:/explorepage";
        }
    }

    @GetMapping({"/messages/{id}"})
    public String conversation(Principal p, Model model, @PathVariable Integer id, @RequestParam(required = false) String text) {
        //Als er geen bericht gestuurd word
        if (text == null || text.length() < 1) {
            try {
                //Gebruiker vinden die ingelogd is
                FashUser loggedIn = users.findFashUserByUsername(p.getName());
                //Eerst kijken of de conversatie al bestaat
                //Convo vinden die gevraagd word
                Conversation conversation = convos.findbyID(id);
                if (conversation.getUsers().contains(loggedIn)) {
                    //Items op read zetten en opslaan in de database
                    conversation.forceOnRead(loggedIn);
                    loggedIn.forceOnRead(conversation);
                    convos.save(conversation);
                    users.save(loggedIn);
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
                //Alle receivers hebben sowieso niet gelezen
                for (FashUser fu:receivers
                ) {
                    //Deze moeten geforceerd op niet gelzen gezet worden!
                    convo.forceNotRead(fu);
                    fu.forceNotRead(convo);
                    users.save(fu);
                }

                //Voor opslaan, de gebruiker er terug inzetten, anders kan hij de conversatie ni meer in!
                receivers.add(sender);
                convo.setUsers(receivers);
                //Tijd van message veranderen voor sorteren
                convo.setLastMessage(LocalDateTime.now());
                //Dingen op gelezen/ongelezen zetten zetten
                convo.forceOnRead(sender);
                sender.forceOnRead(convo);
                users.save(sender);
                convos.save(convo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:/messages/" + id;
        }
    }

    @GetMapping("/checkconvo/{id}")
    public String checkConvo(@PathVariable Integer id, Principal p) {
        String dingetje = "/explorepage";
        try {
            //De twee gebruikers zoeken
            FashUser loggedIn = users.findFashUserByUsername(p.getName());
            FashUser newUser = users.findById(id).get();

            //Als de gebruiker nog geen convo heeft met de gebruiker
            if (!loggedIn.hasConvoWithUser(newUser)) {
                //Een nieuwe convo maken en de nodige dingen doorgeven en opslaan
                Conversation c = new Conversation();
                ArrayList<FashUser> usersconvo=new ArrayList<>();
                c.setConvoNaam(newUser.getUsername());
                usersconvo.add(loggedIn);
                usersconvo.add(newUser);
                c.setUsers(usersconvo);
                loggedIn.addConvo(c);
                newUser.addConvo(c);
                //Dingen opslaan !!!
                convos.save(c);
                users.save(loggedIn);
                users.save(newUser);
                dingetje = "redirect:/messages/" + c.getId();
            } else {
                //System.out.println("We zijn hier");
                dingetje = "redirect:/messages/" + loggedIn.conversationWith(newUser).getId();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return "/profilepage";
        }
        return dingetje;
    }

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
            }else if(currentc.getUsers().size()<=2){
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
            else{
                return "redirect:/explorepage";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/messages";
        }
    }
}
