package com.starbux.starbuxapi.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import com.starbux.starbuxapi.service.Report.ReportService;
import com.starbux.starbuxapi.service.ToppingsService;
import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    ReportService reportService;
    @Autowired
    ToppingsService toppingService;


    @GetMapping("/orders")
    @RolesAllowed("ADMIN")
    public List<Report> Order() {
        return reportService.findAllOrders();
    }


    @GetMapping("/toppings")
    @RolesAllowed("ADMIN")
    public List<Toppings> Toppings() {
        return toppingService.ToppingsReport();
    }

    @DeleteMapping("/DeleteReportByUserId/{userId}")
    @RolesAllowed("ADMIN")
    public void DeleteUserId(@PathVariable long userId){
        reportService.deleteByUserId(userId);
    }



}
