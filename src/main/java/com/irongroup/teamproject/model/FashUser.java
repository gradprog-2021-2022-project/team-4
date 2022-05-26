package com.irongroup.teamproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class FashUser {
    //ID and username are public for all users.
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
    @Id
    public Integer id;
    @NotBlank
    public String username;
    @JsonIgnore
    private String location;
    @JsonIgnore
    @NotBlank
    private String password;

    @JsonIgnore
    private Double longitude;
    @JsonIgnore
    private Double latitude;


    @JsonIgnore
    @Column(length = 10000000)
    private byte[] profilePic;

    @JsonIgnore
    @ManyToMany
    public Collection<FashUser> following;

    @JsonIgnore
    @ManyToMany
    public Collection<FashUser> followers;

    @JsonIgnore
    private String role;
    //These are private unless user wants to share
    @JsonIgnore
    private String first_name;
    @JsonIgnore
    private String last_name;
    @JsonIgnore
    private Integer post_allowance;

    //Posts and comments user has made
    @JsonIgnore
    @OneToMany(mappedBy = "poster", cascade = CascadeType.REMOVE)
    private Collection<FashPost> postsMade;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<FashComment> comments;

    //Clothing that the user has posted and saved
    @JsonIgnore
    @OneToMany(mappedBy = "userOwner")
    private List<Clothing_Item> clothing_posted;
    //Welke kleren zijn opgeslagen?
    @JsonIgnore
    @ManyToMany
    private List<Clothing_Item> clothing_saved;
    //Tot welke conversaties behoort deze persoon?
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Conversation> conversations;
    //Berichtjes verstuurd door deze persoon
    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private Collection<Message> messagesSend;
    //Berichtjes ontvangen door deze persoon
    @JsonIgnore
    @ManyToMany(mappedBy = "receivers")
    private Collection<Message> messagesReceived;


    public FashUser() {
    }

    public FashUser(Integer id, Double latitude, Double longitude, String username, String first_name, String last_name, Integer post_allowance, Collection<FashPost> postsMade, List<FashComment> comments, List<Clothing_Item> clothing_posted, List<Clothing_Item> clothing_saved, String password, String role) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.password = password;
        this.post_allowance = post_allowance;
        this.postsMade = postsMade;
        this.comments = comments;
        this.clothing_posted = clothing_posted;
        this.clothing_saved = clothing_saved;

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void addItem(Clothing_Item item) {
        clothing_saved.add(item);
    }

    public void removeItem(Clothing_Item item) {
        clothing_saved.remove(item);
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPost_allowance(Integer post_allowance) {
        this.post_allowance = post_allowance;
    }

    public void setPostsMade(Collection<FashPost> postsMade) {
        this.postsMade = postsMade;
    }

    public void setComments(List<FashComment> comments) {
        this.comments = comments;
    }

    public void setClothing_posted(List<Clothing_Item> clothing_posted) {
        this.clothing_posted = clothing_posted;
    }

    public void setClothing_saved(List<Clothing_Item> clothing_saved) {
        this.clothing_saved = clothing_saved;
    }
    @JsonIgnore
    public FashPost getLastPost() {
        ArrayList<FashPost> posts = new ArrayList<FashPost>();
        for (FashPost p : postsMade
        ) {
            posts.add(p);
        }
        return posts.get(posts.size() - 1);
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Integer getPost_allowance() {
        return post_allowance;
    }

    public Collection<FashPost> getPostsMade() {
        return postsMade;
    }

    public List<FashComment> getComments() {
        return comments;
    }

    public List<Clothing_Item> getClothing_posted() {
        return clothing_posted;
    }

    public List<Clothing_Item> getClothing_saved() {
        return clothing_saved;
    }

    public void follow(FashUser gebruiker) {
        if (!this.following.contains(gebruiker)) {
            this.following.add(gebruiker);
        }
    }

    public void unFollow(FashUser gebruiker) {
        if (this.following.contains(gebruiker)) {
            this.following.remove(gebruiker);
        }
    }

    public void addFollower(FashUser gebruiker) {
        if (!this.followers.contains(gebruiker)) {
            this.followers.add(gebruiker);
        }
    }

    public void removeFollower(FashUser gebruiker) {
        if (this.followers.contains(gebruiker)) {
            this.followers.remove(gebruiker);
        }
    }

    @JsonIgnore
    public Collection<FashUser> getFollowing() {
        return following;
    }
    @JsonIgnore
    public Collection<FashUser> getFollowers() {
        return followers;
    }
    @JsonIgnore
    public int aantalFollowing() {
        return this.following.size();
    }
    @JsonIgnore
    public int aantalFollowers() {
        return this.followers.size();
    }

    public void setFollowers(Collection<FashUser> followers) {
        this.followers = followers;
    }
    @JsonIgnore
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    @JsonIgnore
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    @JsonIgnore
    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Collection<Message> getMessagesSend() {
        return messagesSend;
    }

    public void setMessagesSend(Collection<Message> messagesSend) {
        this.messagesSend = messagesSend;
    }

    public Collection<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Collection<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }
    @JsonIgnore
    //nakijken of een convo bestaat met een user
    public Boolean hasConvoWithUser(FashUser user) {
        Boolean ja = false;
        try {
            for (Conversation c : conversations
            ) {
                if (c.getUsers().size() == 2) {
                    if (c.getUsers().contains(this) && c.getUsers().contains(user)) {
                        ja = true;
                    }
                }
            }
        } catch (Exception e) {
            //Gene zak
        }
        return ja;
    }
    @JsonIgnore
    //Oproepen van convo met 1 user
    // DONE : BUG FIXEN
    public Conversation conversationWith(FashUser user) {
        //Maak een lege convo
        Conversation convo = null;
        try {
            //Loopen door alle convos van een user
            for (Conversation c : conversations
            ) {
                //Een privegesprek heeft maar 2 gebruikers
                if (c.getUsers().size() <= 2) {
                    //De twee gebruikers zijn de huidige gebruiker en de persoon waar hij mee wil praten
                    if (c.getUsers().contains(user)) {
                        //Direct returnen anders gaan we niet uit de loop en krijg je een error pagina
                        return c;
                    } else {
                        System.out.println("convo is null");
                        convo = null;
                    }
                } else {
                    convo = null;
                }
            }
        } catch (Exception e) {
            //NOG NIKS
        }
        return convo;
    }

    //Een nieuwe convo toevoegen
    public void addConvo(Conversation c) {
        //Kijken of de convo al bestaat, dan moet die niet toegevoegd worden
        if(!this.conversations.contains(c)){
            this.conversations.add(c);
        }
    }
    @JsonIgnore
    //Zowel followers als following vinden
    public Collection<FashUser> findBoth() {
        ArrayList<FashUser> users = new ArrayList<>();
        for (FashUser u : this.followers
        ) {
            users.add(u);
        }
        for (FashUser u : this.following) {
            if(!users.contains(u)){
                users.add(u);
            }
        }
        return users;
    }

    @JsonIgnore
    //Alle posts van volgers vinden
    public List<FashPost> getPostsFromFollowing(){
        List<FashPost> allPosts=new ArrayList<>();
        for (FashUser u:this.following
             ) {
            for (FashPost p:u.getPostsMade()
                 ) {
                allPosts.add(p);
            }
        }
        return allPosts;
    }
    public void putOnRead(Conversation convo){
        //Nog iets aan doen
    }
}
