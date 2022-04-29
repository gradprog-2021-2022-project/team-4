package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<FashUser,Integer> {
    Collection<FashUser> findAll();
}
