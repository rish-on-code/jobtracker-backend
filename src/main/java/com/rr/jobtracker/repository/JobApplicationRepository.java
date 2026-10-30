package com.rr.jobtracker.repository;

import com.rr.jobtracker.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    List<JobApplication> findByUser_Id(Long userId);

    List<JobApplication> findByStatusIgnoreCase(String status);

    Long countByStatusIgnoreCase(String status);

    boolean existsByJob_Id(Long jobId);
}
