package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashPost;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<FashPost,Integer> {
}
