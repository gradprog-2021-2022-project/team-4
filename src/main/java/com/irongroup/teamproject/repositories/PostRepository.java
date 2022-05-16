package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashPost;
import com.irongroup.teamproject.model.FashUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PostRepository extends CrudRepository<FashPost,Integer> {
    Collection<FashPost> findAll();

    @Query("select p from FashPost p where p.poster.id = :user")
    Collection<FashPost> findbyUserId(@Param("user") Integer user);

    /*
    @Query("select f,p from FashUser f,FashPost p where f.postsMade.size>1 and lower(p.stijl) = lower(:stijl)")
    Collection<FashUser> findByStyle(@Param("stijl") String stijl);*/
}
