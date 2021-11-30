package com.starbux.starbuxapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.starbux.starbuxapi.entity.Report;

import javax.transaction.Transactional;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {



    @Modifying
    @Transactional
    @Query("UPDATE Report r set r.orders = r.orders + 1 WHERE r.user.id = :user_id")
    void addRecordByUserId(@Param("user_id")Long user_id);
    boolean existsByUser_Id(long userId);

    void deleteByUserId(long userId);

}
