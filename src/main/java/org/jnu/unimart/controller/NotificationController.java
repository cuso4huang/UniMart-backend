package org.jnu.unimart.controller;

import org.jnu.unimart.pojo.Notification;
import org.jnu.unimart.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    // 服务器主动发送通知的方法，客户端订阅/topic/notifications/{userId}
    public void sendSystemNotification(Integer userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }

    // 示例：客户端发送通知请求到服务器（可选）
    @MessageMapping("/sendNotification")
    public void handleSendNotification(Notification notification) {
        // 处理客户端发送的通知（如果需要）
    }
}
