package com.irongroup.teamproject.repositories;

import com.irongroup.teamproject.model.FashUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<FashUser,Integer> {
}
