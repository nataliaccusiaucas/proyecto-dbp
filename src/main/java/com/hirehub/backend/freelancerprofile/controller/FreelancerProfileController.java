package com.hirehub.backend.freelancerprofile.controller;

import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileResponseDTO;
import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileUpdateDTO;
import com.hirehub.backend.freelancerprofile.service.FreelancerProfileService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{freelancerId}/profile")
    public ResponseEntity<FreelancerProfileResponseDTO> getProfile(@PathVariable UUID freelancerId) {
        return ResponseEntity.ok(profileService.getProfileByFreelancer(freelancerId));
    }

    @PutMapping("/{freelancerId}/profile")
    public ResponseEntity<FreelancerProfileResponseDTO> updateProfile(
            @PathVariable UUID freelancerId,
            @RequestBody FreelancerProfileUpdateDTO dto
    ) {
        return ResponseEntity.ok(profileService.updateProfile(freelancerId, dto));
    }
}