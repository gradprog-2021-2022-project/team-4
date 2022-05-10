package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.Clothing_Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ClothingRepository extends CrudRepository<Clothing_Item,Integer> {
    Collection<Clothing_Item> findAll();

    @Query("select c from Clothing_Item c where c.userOwner.id = :user group by c.post")
    Collection<Clothing_Item> findClothingOfUser(@Param("user")Integer userID);


    @Query("select c from Clothing_Item c where c.userOwner.id= :user and (:kledingname is null or lower(c.naam) like lower(concat('%',:kledingname,'%')))")
    Collection<Clothing_Item> findClothingByFilter(@Param("user") Integer userID,@Param("kledingname") String kledingname);
}
