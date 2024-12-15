package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Notification;
import org.jnu.unimart.repository.NotificationRepository;
import org.jnu.unimart.service.NotificationService;
import org.jnu.unimart.service.NotificationWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketService notificationWebSocketService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationWebSocketService notificationWebSocketService) {
        this.notificationRepository = notificationRepository;
        this.notificationWebSocketService = notificationWebSocketService;
    }

    @Override
    public Notification createNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        // 发送实时通知
        notificationWebSocketService.sendNotification(notification.getUserId(), savedNotification);
        return savedNotification;
    }

    @Override
    public List<Notification> getNotificationsForUser(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
