package com.rr.jobtracker.controller;

import com.rr.jobtracker.entity.Job;
import com.rr.jobtracker.service.JobService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job){
        try{
            Job savedJob = jobService.createJob(job);
            return ResponseEntity
                    .status(201)
                    .body(savedJob);
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }



    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        try {
            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Job> jobs = jobService.getAllJobs(pageable);

            return ResponseEntity.ok(jobs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
        try{
            Job job = jobService.getJobById(id);
            return ResponseEntity.ok(job);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job ){
        try {
            Job updatedJob = jobService.updateJob(id, job);
            return ResponseEntity.ok(updatedJob);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Job> closeJob(@PathVariable Long id){
        try{
            Job job = jobService.closeJob(id);
            return ResponseEntity
                    .ok(job);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        try {
            String message = jobService.deleteJob(id);
            return ResponseEntity.ok(message);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(e.getMessage());
        }
    }




    @GetMapping("/search/title")
    public List<Job> searchJobsByTitle(
            @RequestParam String title) {

        return jobService.searchJobsByTitle(title);
    }

    @GetMapping("/search/company")
    public List<Job> searchJobsByCompany(
            @RequestParam String company) {

        return jobService.searchJobsByCompany(company);
    }


}
