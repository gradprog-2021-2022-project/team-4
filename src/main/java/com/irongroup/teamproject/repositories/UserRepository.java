package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<FashUser,Integer> {
    Collection<FashUser> findAll();
    FashUser findFashUserByUsername(String username);

    @Query("select u from FashUser u where (u.postsMade.size>=1)")
    Collection<FashUser> findUsersWithPosts();

    FashUser findById(int id);
}
