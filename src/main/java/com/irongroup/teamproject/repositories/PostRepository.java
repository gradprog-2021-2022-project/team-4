package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashPost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PostRepository extends CrudRepository<FashPost,Integer> {
    Collection<FashPost> findAll();

    @Query("select p from FashPost p where p.poster.id = :user")
    Collection<FashPost> findbyUserId(@Param("user") Integer user);

    @Query("select p from FashPost p where lower(p.stijl) = lower(:stijl)")
    Collection<FashPost> findByStyle(@Param("stijl") String stijl);
}
