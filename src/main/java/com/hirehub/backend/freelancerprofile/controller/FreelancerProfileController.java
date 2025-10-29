package com.hirehub.backend.freelancerprofile.controller;

import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileResponseDTO;
import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileUpdateDTO;
import com.hirehub.backend.freelancerprofile.service.FreelancerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/freelancers")
@CrossOrigin(origins = "*")
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    public FreelancerProfileController(FreelancerProfileService profileService) {
        this.profileService = profileService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{freelancerId}/profile")
    public ResponseEntity<FreelancerProfileResponseDTO> getProfile(@PathVariable UUID freelancerId) {
        return ResponseEntity.ok(profileService.getProfileByFreelancer(freelancerId));
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{freelancerId}/profile")
    public ResponseEntity<FreelancerProfileResponseDTO> updateProfile(
            @PathVariable UUID freelancerId,
            @RequestBody FreelancerProfileUpdateDTO dto
    ) {
        return ResponseEntity.ok(profileService.updateProfile(freelancerId, dto));
    }
}