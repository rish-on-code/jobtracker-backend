package com.rr.jobtracker.service;

import com.rr.jobtracker.repository.JobApplicationRepository;
import com.rr.jobtracker.repository.JobRepository;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    public Map<String, Long> getStatistics(){
        Map<String, Long> statistics = new HashMap<>();

        statistics.put("totalJobs", jobRepository.count());

        statistics.put("totalApplications", jobApplicationRepository.count());

        statistics.put("shortlisted", jobApplicationRepository.countByStatusIgnoreCase("SHORTLISTED"));

        statistics.put("rejected", jobApplicationRepository.countByStatusIgnoreCase("REJECTED"));

        statistics.put("selected", jobApplicationRepository.countByStatusIgnoreCase("SELECTED"));

        return statistics;
    }
}
