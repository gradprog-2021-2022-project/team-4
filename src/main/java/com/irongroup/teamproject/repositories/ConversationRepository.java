package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.Conversation;
import com.irongroup.teamproject.model.FashUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ConversationRepository extends CrudRepository<Conversation,Integer> {
    //Convo kunnen vinden om weer te geven
    @Query("select c from Conversation c where c.id= :id")
    Conversation findbyID(@Param("id")Integer id);

    //Optional om nieuwe convos te kunnen starten
    @Query("select c.id from Conversation c")
    Collection<Integer> findOptByID(@Param("id") Integer id);

    //Message size vinden
    @Query("select c.messages.size from Conversation c where c.id = :id")
    Integer findsizeofconvo(@Param("id") Integer id);

    /*werkt niet
    @Query("select c from Conversation c where :user in c.users")
    Collection<Conversation> findConvosByUser(@Param("user")FashUser user);*/
}
