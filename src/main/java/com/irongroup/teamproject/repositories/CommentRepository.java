package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashComment;
import com.irongroup.teamproject.model.FashPost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Collection;

public interface CommentRepository extends CrudRepository<FashComment,Integer> {
    Collection<FashComment> findAll();

    @Query("select c from FashComment c where (c.post = :post) order by c.date desc")
    ArrayList<FashComment> findCommentsForPost(@Param("post") FashPost post);

    @Query("select c from FashComment c where c.user.id = :id")
    Collection<FashComment> findFashCommentByUserID(@Param("id") Integer id);
}
