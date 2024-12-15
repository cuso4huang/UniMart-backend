package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(Notification notification);
    List<Notification> getNotificationsForUser(Integer userId);
    void markAsRead(Integer notificationId);
}
