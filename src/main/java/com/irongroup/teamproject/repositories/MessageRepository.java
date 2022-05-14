package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Integer> {
}
