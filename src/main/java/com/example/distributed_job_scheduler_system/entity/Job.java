package com.example.distributed_job_scheduler_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Job {
   @Id
    private Long jobId;
   private String payload;
   @Enumerated(EnumType.STRING)
   private JobStatus jobStatus;
   private Integer retryCount;
   private Integer workerId;
}

