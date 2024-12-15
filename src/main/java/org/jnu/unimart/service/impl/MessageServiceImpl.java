package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Message;
import org.jnu.unimart.repository.MessageRepository;
import org.jnu.unimart.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public Message sendMessage(Integer senderId, Integer receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setCreateTime(LocalDateTime.now());
        message.setRead(false);  // 新消息默认未读

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatHistory(Integer userId1, Integer userId2) {
        // 获取两个用户之间的所有消息
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreateTimeDesc(
                userId1, userId2, userId1, userId2);
    }

    @Override
    public List<Message> getAllMessages(Integer userId) {
        // 获取用户发送或接收的所有消息
        return messageRepository.findBySenderIdOrReceiverIdOrderByCreateTimeDesc(userId, userId);
    }

    @Override
    public List<Message> getUserMessages(Integer userId) {
        // 获取用户接收的所有消息
        return messageRepository.findByReceiverIdOrderByCreateTimeDesc(userId);
    }

    @Override
    public int getUnreadCount(Integer userId) {
        // 获取用户未读消息数量
        return messageRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Integer messageId, Integer userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        // 确保只有接收者可以标记消息为已读
        if (!message.getReceiverId().equals(userId)) {
            throw new RuntimeException("无权操作此消息");
        }

        message.setRead(true);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(Integer messageId, Integer userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        // 确保只有发送者或接收者可以删除消息
        if (!message.getSenderId().equals(userId) && !message.getReceiverId().equals(userId)) {
            throw new RuntimeException("无权删除此消息");
        }

        messageRepository.delete(message);
    }
}