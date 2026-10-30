
package com.rr.jobtracker.service;

import com.rr.jobtracker.entity.Job;
import com.rr.jobtracker.repository.JobApplicationRepository;
import com.rr.jobtracker.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    // Create a new job
    public Job createJob(Job job) {
        if (job.getStatus() == null || job.getStatus().isBlank()) {
            job.setStatus("OPEN");
        }

        return jobRepository.save(job);
    }

    // Get all jobs with pagination
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    // Get a job by ID
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + id));
    }

    // Update an existing job
    public Job updateJob(Long id, Job updatedJob) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + id));

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setCompany(updatedJob.getCompany());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setSalary(updatedJob.getSalary());

        if (updatedJob.getStatus() != null
                && !updatedJob.getStatus().isBlank()) {
            existingJob.setStatus(updatedJob.getStatus());
        }

        return jobRepository.save(existingJob);
    }

    // Close a job
    public Job closeJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + id));

        job.setStatus("CLOSED");
        return jobRepository.save(job);
    }


    // Delete a job
    public String deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + id));

        boolean hasApplications =
                jobApplicationRepository.existsByJob_Id(id);

        if (hasApplications) {
            jobRepository.updateJobStatus(id, "CLOSED");

            return "Job has applications. Job status changed to CLOSED.";
        }

        jobRepository.delete(job);

        return "Job deleted successfully.";
    }



    // Search jobs by title
    public List<Job> searchJobsByTitle(String title) {
        return jobRepository.findByTitleContainingIgnoreCase(title);
    }

    // Search jobs by company
    public List<Job> searchJobsByCompany(String company) {
        return jobRepository.findByCompanyContainingIgnoreCase(company);
    }
}
