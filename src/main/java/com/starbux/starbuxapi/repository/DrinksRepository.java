package com.starbux.starbuxapi.repository;

import com.starbux.starbuxapi.entity.Drinks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinksRepository extends JpaRepository<Drinks,Integer> {
    Drinks findByName(String name);


}
