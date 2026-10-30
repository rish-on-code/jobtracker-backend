
package com.rr.jobtracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Job location is required")
    private String location;

    @NotBlank(message = "Job description is required")
    @Column(length = 3000)
    private String description;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary cannot be negative")
    private Double salary;

    @NotBlank(message = "Job status is required")
    private String status;
}
