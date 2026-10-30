
package com.rr.jobtracker.service;

import com.rr.jobtracker.dto.JobApplicationResponse;
import com.rr.jobtracker.entity.Job;
import com.rr.jobtracker.entity.JobApplication;
import com.rr.jobtracker.entity.User;
import com.rr.jobtracker.repository.JobApplicationRepository;
import com.rr.jobtracker.repository.JobRepository;
import com.rr.jobtracker.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public JobApplicationResponse applyForJob(
            Long jobId, String coverLetter) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (!"OPEN".equalsIgnoreCase(job.getStatus())) {
            throw new RuntimeException(
                    "This job is not open for applications");
        }

        if (jobApplicationRepository.existsByUserIdAndJobId(
                user.getId(), jobId)) {
            throw new RuntimeException(
                    "You have already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setCoverLetter(coverLetter);
        application.setStatus("APPLIED");

        JobApplication savedApplication =
                jobApplicationRepository.save(application);

        return convertToResponse(savedApplication);
    }

    public List<JobApplicationResponse> getMyApplications() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<JobApplication> applications =
                jobApplicationRepository.findByUser_Id(user.getId());

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public JobApplicationResponse updateApplicationStatus(
            Long applicationId, String status) {

        if (status == null ||
                !List.of("APPLIED", "SHORTLISTED", "REJECTED", "SELECTED")
                        .contains(status.toUpperCase())) {
            throw new RuntimeException("Invalid application status");
        }

        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        application.setStatus(status.toUpperCase());

        JobApplication savedApplication =
                jobApplicationRepository.save(application);

        return convertToResponse(savedApplication);
    }

    public List<JobApplicationResponse> getAllApplications() {

        List<JobApplication> applications =
                jobApplicationRepository.findAll();

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<JobApplicationResponse> getApplicationsByStatus(
            String status) {

        List<JobApplication> applications =
                jobApplicationRepository.findByStatusIgnoreCase(status);

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private JobApplicationResponse convertToResponse(
            JobApplication application) {

        JobApplicationResponse response =
                new JobApplicationResponse();

        response.setId(application.getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());

        // Applicant ki details
        response.setUserName(application.getUser().getName());
        response.setUserEmail(application.getUser().getEmail());

        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        response.setCoverLetter(application.getCoverLetter());

        return response;
    }
}
