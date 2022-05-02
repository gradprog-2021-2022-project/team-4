package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashPost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PostRepository extends CrudRepository<FashPost,Integer> {
    Collection<FashPost> findAll();
}
