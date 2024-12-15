package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Message;
import java.util.List;

public interface MessageService {
    Message sendMessage(Integer senderId, Integer receiverId, String content);

    List<Message> getChatHistory(Integer userId1, Integer userId2);

    List<Message> getAllMessages(Integer userId);

    int getUnreadCount(Integer userId);

    void markAsRead(Integer messageId, Integer userId);

    void deleteMessage(Integer messageId, Integer userId);

    List<Message> getUserMessages(Integer userId);
}