package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 发送系统通知到指定用户
     *
     * @param userId 用户ID
     * @param notification 通知内容
     */
    public void sendNotification(Integer userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }
}
