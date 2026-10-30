package com.rr.jobtracker.controller;

import com.rr.jobtracker.dto.ApplyJobRequest;
import com.rr.jobtracker.dto.JobApplicationResponse;
import com.rr.jobtracker.dto.UpdateApplicationStatusRequest;
import com.rr.jobtracker.entity.JobApplication;
import com.rr.jobtracker.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("{jobId}")
    public ResponseEntity<JobApplicationResponse> applyForJob(@PathVariable Long jobId, @RequestBody ApplyJobRequest request){

        JobApplicationResponse application = jobApplicationService.applyForJob(jobId, request.getCoverLetter());

        return ResponseEntity.ok(application);

    }

    @GetMapping("/my")
    public ResponseEntity<List<JobApplicationResponse>> getMyApplications() {

        List<JobApplicationResponse> applications= jobApplicationService.getMyApplications();

        return ResponseEntity.ok(applications);
    }

    @PatchMapping("{applicationId}/status")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(@PathVariable Long applicationId, @RequestBody UpdateApplicationStatusRequest request){
        JobApplicationResponse response = jobApplicationService.updateApplicationStatus(applicationId, request.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications(){
        List<JobApplicationResponse> applications = jobApplicationService.getAllApplications();

        return ResponseEntity.ok(applications);
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<JobApplicationResponse>> getApplicationByStatus(@PathVariable String status){
        List<JobApplicationResponse> applications = jobApplicationService.getApplicationsByStatus(status);

        return ResponseEntity.ok(applications);
    }

}
