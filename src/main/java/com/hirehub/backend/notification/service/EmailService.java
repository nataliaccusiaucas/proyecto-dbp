package com.hirehub.backend.notification.service;

import com.hirehub.backend.common.exception.InternalServerException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("notificaciones@hirehub.app");
            mailSender.send(message);
        } catch (MailException ex) {
            throw new InternalServerException("Error al enviar correo a " + to);
        }
    }
}
