package com.placement.portal.service;

import com.placement.portal.dto.NotificationResponse;
import com.placement.portal.entity.Notification;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    /**
     * Internal helper used by OTHER services (ApplicationService, InterviewService)
     * to create a notification whenever something noteworthy happens.
     */
    public void notify(String recipientEmail, String recipientType, String title, String message, String type) {
        Notification notification = new Notification();
        notification.setRecipientEmail(recipientEmail);
        notification.setRecipientType(recipientType);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notificationRepository.save(notification);

        // Send a real email alongside the in-app notification.
        // This single line is what powers the entire "Email Alert" feature
        // for every event in the system -- new applications, status changes,
        // interview scheduling, and interview results.
        emailService.sendEmail(recipientEmail, title, message);
    }

    public List<NotificationResponse> getMyNotifications(String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(String email) {
        return notificationRepository.countByRecipientEmailAndIsReadFalse(email);
    }

    public NotificationResponse markAsRead(String email, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException("Notification not found"));

        // SECURITY CHECK: a user can only mark THEIR OWN notifications as read
        if (!notification.getRecipientEmail().equals(email)) {
            throw new ApiException("You are not authorized to update this notification");
        }

        notification.setRead(true);
        Notification saved = notificationRepository.save(notification);
        return toResponse(saved);
    }

    public void markAllAsRead(String email) {
        List<Notification> notifications = notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getType(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}