package com.starbux.starbuxapi.service.Report;

import org.springframework.stereotype.Service;

import com.starbux.starbuxapi.entity.Report;

import java.util.List;

@Service
public interface ReportService{

    void RecordOrderByUserId(long userId);
    boolean existsByUser_Id(long userId);
    void addRecordByUserId(long userId);
    List<Report> findAllOrders();
    void deleteByUserId(long userId);

}
