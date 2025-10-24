package com.hirehub.backend.notification.event;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.event.OfferAcceptedEvent;
import com.hirehub.backend.notification.domain.NotificationType;
import com.hirehub.backend.notification.service.NotificationService;
import com.hirehub.backend.notification.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final EmailService emailService;

    public NotificationEventListener(NotificationService notificationService, EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @EventListener
    public void handleOfferAccepted(OfferAcceptedEvent event) {
        Offer offer = event.getOffer();

        String message = "🎉 Tu oferta para '" + offer.getJobRequest().getTitle() + "' fue aceptada.";
        notificationService.createNotification(
                NotificationType.OFFER_ACCEPTED,
                message,
                offer.getFreelancer()
        );

        emailService.sendEmail(
                offer.getFreelancer().getEmail(),
                "Oferta aceptada en HireHub",
                "Hola " + offer.getFreelancer().getName() +
                ",\n\nTu oferta para el trabajo '" + offer.getJobRequest().getTitle() +
                "' fue aceptada por el cliente.\n\n" +
                "Ingresa a HireHub para coordinar los siguientes pasos.\n\n" +
                "¡Felicidades!\nEl equipo de HireHub 🌟"
        );
    }
}
