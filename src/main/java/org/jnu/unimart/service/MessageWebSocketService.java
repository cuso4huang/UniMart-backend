package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 发送用户消息到指定接收者
     *
     * @param receiverId 接收者用户ID
     * @param message 消息内容
     */
    public void sendMessage(Integer receiverId, Message message) {
        messagingTemplate.convertAndSend("/topic/messages/" + receiverId, message);
    }
}
