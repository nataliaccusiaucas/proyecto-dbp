package com.hirehub.backend.notification.event;

import com.hirehub.backend.freelancerprofile.repository.FreelancerProfileRepository;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.event.JobRequestCreatedEvent;
import com.hirehub.backend.notification.domain.NotificationType;
import com.hirehub.backend.notification.service.NotificationService;
import com.hirehub.backend.notification.service.EmailService;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class JobRequestNotificationListener {

    private final UserRepository userRepository;
    private final FreelancerProfileRepository profileRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public JobRequestNotificationListener(
            UserRepository userRepository,
            FreelancerProfileRepository profileRepository,
            NotificationService notificationService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @EventListener
    public void onJobRequestCreated(JobRequestCreatedEvent event) {

        JobRequest jobRequest = event.getJobRequest();
        String jobText = (jobRequest.getTitle() + " " + jobRequest.getDescription()).toLowerCase();

        List<User> freelancers = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == Role.FREELANCER)
                .toList();


        List<User> matchedFreelancers = freelancers.stream()
                .filter(u -> {
                    var profile = profileRepository.findByUser(u).orElse(null);
                    if (profile == null || profile.getSkills() == null) return false;

                    return Arrays.stream(profile.getSkills().toLowerCase().split(","))
                            .map(String::trim)
                            .anyMatch(jobText::contains);
                })
                .toList();


        matchedFreelancers.forEach(freelancer -> {

            String msg = "Nuevo trabajo disponible: " + jobRequest.getTitle();

            notificationService.createNotification(
                    NotificationType.JOB_REQUEST_CREATED,
                    msg,
                    freelancer
            );

            emailService.sendEmail(
                    freelancer.getEmail(),
                    "Nuevo trabajo en tu categoría",
                    "Hola " + freelancer.getName()
                            + "Hay un nuevo trabajo que coincide con tu categoría:"
                            + jobRequest.getTitle()
                            + "Ingresa a HireHub para enviar una propuesta."
            );
        });
    }
}
