package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.Clothing_Item;
import org.springframework.data.repository.CrudRepository;

public interface ClothingRepository extends CrudRepository<Clothing_Item,Integer> {
}
