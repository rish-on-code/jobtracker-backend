package com.rr.jobtracker.repository;

import com.rr.jobtracker.entity.Job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {


    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByCompanyContainingIgnoreCase(String company);

    @Modifying
    @Transactional
    @Query("UPDATE Job j SET j.status = :status WHERE j.id = :id")
    int updateJobStatus(
            @Param("id") Long id,
            @Param("status") String status
    );

}
