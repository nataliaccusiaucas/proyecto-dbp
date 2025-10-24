package com.hirehub.backend.freelancerprofile.service;

import com.hirehub.backend.freelancerprofile.domain.FreelancerProfile;
import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileResponseDTO;
import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileUpdateDTO;
import com.hirehub.backend.freelancerprofile.repository.FreelancerProfileRepository;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class FreelancerProfileService {

    private final FreelancerProfileRepository profileRepository;
    private final UserRepository userRepository;

    public FreelancerProfileService(FreelancerProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public FreelancerProfileResponseDTO getProfileByFreelancer(UUID freelancerId) {
        var user = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado"));
        var profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado"));

        return new FreelancerProfileResponseDTO(
                profile.getId(),
                user.getName(),
                profile.getTitle(),
                profile.getDescription(),
                profile.getSkills(),
                profile.getPortfolioUrl(),
                profile.getLocation(),
                user.getAverageRating(),
                user.getCompletedJobs()
        );
    }

    public FreelancerProfileResponseDTO updateProfile(UUID freelancerId, FreelancerProfileUpdateDTO dto) {
        var user = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado"));

        // ✅ Si el perfil no existe, se crea automáticamente
        var profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    var newProfile = new FreelancerProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setTitle(dto.title());
        profile.setDescription(dto.description());
        profile.setSkills(dto.skills());
        profile.setPortfolioUrl(dto.portfolioUrl());
        profile.setLocation(dto.location());

        profileRepository.save(profile);

        return new FreelancerProfileResponseDTO(
                profile.getId(),
                user.getName(),
                profile.getTitle(),
                profile.getDescription(),
                profile.getSkills(),
                profile.getPortfolioUrl(),
                profile.getLocation(),
                user.getAverageRating(),
                user.getCompletedJobs()
        );
    }
}