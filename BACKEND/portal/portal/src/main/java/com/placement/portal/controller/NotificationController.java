package com.placement.portal.controller;

import com.placement.portal.dto.NotificationResponse;
import com.placement.portal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Works for BOTH students and companies -- whoever is logged in
 * only ever sees their own notifications.
 *
 *   GET /api/notifications              -> get my notifications (most recent first)
 *   GET /api/notifications/unread-count -> get count of unread notifications
 *   PUT /api/notifications/{id}/read    -> mark one notification as read
 *   PUT /api/notifications/read-all     -> mark all as read
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(notificationService.getMyNotifications(email));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication authentication) {
        String email = authentication.getName();
        long count = notificationService.getUnreadCount(email);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            Authentication authentication,
            @PathVariable Long id) {
        String email = authentication.getName();
        return ResponseEntity.ok(notificationService.markAsRead(email, id));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        String email = authentication.getName();
        notificationService.markAllAsRead(email);
        return ResponseEntity.noContent().build();
    }
}