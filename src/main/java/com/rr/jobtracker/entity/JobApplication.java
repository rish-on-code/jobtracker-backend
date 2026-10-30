package com.rr.jobtracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"user_id", "job_id"}
        )
})
@Data
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private String status = "APPLIED";

    private LocalDateTime appliedAt = LocalDateTime.now();

    @Column(length = 2000)
    private String coverLetter;
}
