package com.starbux.starbuxapi.repository;

import com.starbux.starbuxapi.entity.Toppings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface ToppingsRepository extends JpaRepository<Toppings,Integer> {
    Toppings findByName(String name);
    List<Toppings> findAllByNameStartsWith(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Toppings t set t.orders = t.orders + 1 WHERE t.id = :toppings_id")
    void addRecordByUserId(@Param("toppings_id")int toppings_id);
    List<Toppings> findAllByOrderByOrdersDesc();
}
