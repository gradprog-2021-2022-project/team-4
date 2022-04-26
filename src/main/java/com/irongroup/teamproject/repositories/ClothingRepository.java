package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.Clothing_Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ClothingRepository extends CrudRepository<Clothing_Item,Integer> {
    Collection<Clothing_Item> findAll();
}
