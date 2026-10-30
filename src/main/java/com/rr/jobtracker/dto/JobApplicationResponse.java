
package com.rr.jobtracker.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobApplicationResponse {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String company;

    private String userName;
    private String userEmail;

    private String status;
    private LocalDateTime appliedAt;
    private String coverLetter;
}
