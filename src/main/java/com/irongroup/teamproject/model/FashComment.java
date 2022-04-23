package com.irongroup.teamproject.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class FashComment {

    //ID
    @Id
    public Integer id;

    //User that has made the comment
    @OneToOne
    private FashUser poster;

    //Post on which the comment was made
    @OneToOne
    private FashPost post;

    //Basic info of the post itself
    private String title;
    private String text;
    private LocalDate date;
    private LocalTime time;

    public FashComment() {
    }
}
