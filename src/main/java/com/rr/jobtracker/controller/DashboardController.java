package com.rr.jobtracker.controller;

import com.rr.jobtracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("statistics")
    public ResponseEntity<Map<String, Long>> getStatistics(){
        Map<String, Long> statistics = dashboardService.getStatistics();

        return ResponseEntity.ok(statistics);
    }
}
