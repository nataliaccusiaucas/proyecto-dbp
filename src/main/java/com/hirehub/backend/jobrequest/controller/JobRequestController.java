package com.hirehub.backend.jobrequest.controller;

import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.dto.JobRequestRequestDTO;
import com.hirehub.backend.jobrequest.dto.JobRequestResponseDTO;
import com.hirehub.backend.jobrequest.service.JobRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-requests")
@CrossOrigin(origins = "*")
public class JobRequestController {

    private final JobRequestService jobRequestService;

    public JobRequestController(JobRequestService jobRequestService) {
        this.jobRequestService = jobRequestService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<JobRequestResponseDTO> createJobRequest(@Valid @RequestBody JobRequestRequestDTO request) {
        JobRequest jobRequest = jobRequestService.createJobRequest(
                request.title(),
                request.description(),
                request.budget(),
                request.clientId()
        );
        return ResponseEntity.ok(mapToResponse(jobRequest));
    }

    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @PatchMapping("/{jobRequestId}/complete")
    public ResponseEntity<JobRequest> markJobAsCompleted(@PathVariable UUID jobRequestId) {
        return ResponseEntity.ok(jobRequestService.markAsCompleted(jobRequestId));
    }
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','FREEELANCER')")
    @GetMapping
    public ResponseEntity<List<JobRequestResponseDTO>> getAllJobRequests() {
        List<JobRequestResponseDTO> response = jobRequestService.getAllJobRequests()
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'FREELANCER')")
    @GetMapping("/{id}")
    public ResponseEntity<JobRequestResponseDTO> getJobRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(mapToResponse(jobRequestService.getJobRequestById(id)));
    }

    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<JobRequestResponseDTO>> getJobRequestsByClient(@PathVariable UUID clientId) {
        List<JobRequestResponseDTO> response = jobRequestService.getJobRequestsByClient(clientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    private JobRequestResponseDTO mapToResponse(JobRequest jobRequest) {
        return new JobRequestResponseDTO(
                jobRequest.getId(),
                jobRequest.getTitle(),
                jobRequest.getDescription(),
                jobRequest.getBudget(),
                jobRequest.getStatus(),
                jobRequest.getClient().getId(),
                jobRequest.getClient().getName(),
                jobRequest.getCreatedAt()
        );
    }
}
