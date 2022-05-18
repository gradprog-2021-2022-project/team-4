package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message,Integer> {
    @Query("select m.id from Message m order by m.id desc")
    ArrayList<Integer> findHighestID();
}
