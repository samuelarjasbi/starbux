package com.starbux.starbuxapi.service.Report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starbux.starbuxapi.repository.ReportRepository;
import com.starbux.starbuxapi.repository.UserRepository;
import com.starbux.starbuxapi.entity.Report;
import com.starbux.starbuxapi.entity.User;

import java.util.List;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReportRepository reportRepository;

    @Override
    public void RecordOrderByUserId(long userId){
        reportRepository.addRecordByUserId(userId);
    }

    @Override
    public boolean existsByUser_Id(long userId){
        return reportRepository.existsByUser_Id(userId);
    }

    @Override
    public List<Report> findAllOrders(){
        return reportRepository.findAll();
    }

    @Override
    public void deleteByUserId(long userId){
        reportRepository.deleteByUserId(userId);
    }

    @Override
    public void addRecordByUserId(long userId){
        User user = userRepository.findById(userId);
        Report rep = new Report();

        rep.setUser(user);
        reportRepository.save(rep);
    }

}
