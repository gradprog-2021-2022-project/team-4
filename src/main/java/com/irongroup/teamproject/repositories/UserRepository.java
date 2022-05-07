package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends CrudRepository<FashUser,Integer> {
    Collection<FashUser> findAll();
    FashUser findFashUserByUsername(String username);

    @Query("select u from FashUser u where (:username IS null or u.username = :username)")
    Optional<FashUser> findUserOptional(@Param("username") String username);

    @Query("select u from FashUser u where (u.postsMade.size>=1)")
    Collection<FashUser> findUsersWithPosts();

    FashUser findById(int id);
}
