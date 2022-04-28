package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashComment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<FashComment,Integer> {
}
