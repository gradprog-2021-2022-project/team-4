package com.irongroup.teamproject.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
public class FashUser {
    //ID and username are public for all users.
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
    @Id
    public Integer id;
    @NotBlank
    public String username;
    private String location;
    @NotBlank
    private String password;

    private Double longitude;
    private Double latitude;

    @Column(length = 10000000)
    private byte[] profilePic;

    @ManyToMany
    public Collection<FashUser> following;

    @ManyToMany
    public Collection<FashUser> followers;

    private String role;
    //These are private unless user wants to share
    private String first_name;
    private String last_name;
    private Integer post_allowance;

    //Posts and comments user has made
    @OneToMany(mappedBy = "poster", cascade = CascadeType.REMOVE)
    private Collection<FashPost> postsMade;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<FashComment> comments;

    //Clothing that the user has posted and saved
    @OneToMany(mappedBy = "userOwner")
    private List<Clothing_Item> clothing_posted;
    //Welke kleren zijn opgeslagen?
    @ManyToMany
    private List<Clothing_Item> clothing_saved;
    //Tot welke conversaties behoort deze persoon?
    @ManyToMany(mappedBy = "users")
    private Collection<Conversation> conversations;
    //Berichtjes verstuurd door deze persoon
    @OneToMany(mappedBy = "sender")
    private Collection<Message> messagesSend;
    //Berichtjes ontvangen door deze persoon
    @ManyToMany(mappedBy = "receivers")
    private Collection<Message> messagesReceived;


    public FashUser() {
    }

    public FashUser(Integer id, Double latitude, Double longitude, String username, String location, String first_name, String last_name, Integer post_allowance, Collection<FashPost> postsMade, List<FashComment> comments, List<Clothing_Item> clothing_posted, List<Clothing_Item> clothing_saved, String password, String role) {
        this.id = id;
        this.username = username;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Collection<FashUser> getFollowing() {
        return following;
    }

    public Collection<FashUser> getFollowers() {
        return followers;
    }

    public int aantalFollowing() {
        return this.following.size();
    }

    public int aantalFollowers() {
        return this.followers.size();
    }

    public void setFollowers(Collection<FashUser> followers) {
        this.followers = followers;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Collection<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Collection<Conversation> conversations) {
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

    //nakijken of een convo bestaat met een user
    public Boolean hasConvoWithUser(FashUser user){
        Boolean ja=false;
        try{
            for (Conversation c:conversations
                 ) {
                if(c.getUsers().size()==2){
                    if(c.getUsers().contains(this) && c.getUsers().contains(user))
                    {
                        ja=true;
                    }
                }
            }
        }catch (Exception e){
            //Gene zak
        }
        return ja;
    }

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
                if (c.getUsers().size() == 2) {
                    //De twee gebruikers zijn de huidige gebruiker en de persoon waar hij mee wil praten
                    if (c.getUsers().contains(this) && c.getUsers().contains(user)) {
                        convo = c;
                    }
                }
            }
        } catch (Exception e) {
            //NOG NIKS
        }
        return convo;
    }
    //Een nieuwe convo toevoegen
    public void addConvo(Conversation c){
        this.conversations.add(c);
    }
}
