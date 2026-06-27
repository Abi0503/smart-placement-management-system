package com.placement.portal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends real emails via Gmail SMTP, using the spring-boot-starter-mail
 * dependency and the credentials configured in application.properties.
 *
 * JavaMailSender is auto-configured by Spring Boot purely from the
 * spring.mail.* properties -- we don't need to manually build it.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            // Deliberately just log instead of throwing -- a failed email
            // (e.g. internet hiccup) should NEVER break the actual feature
            // (applying to a job, scheduling an interview, etc.)
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
