package org.jnu.unimart.controller;

import org.jnu.unimart.payload.MessageRequest;
import org.jnu.unimart.pojo.Message;
import org.jnu.unimart.security.UserDetailsImpl;
import org.jnu.unimart.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 已有的发送消息接口
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Message message = messageService.sendMessage(
                currentUser.getId(),
                request.getReceiverId(),
                request.getContent()
        );
        return ResponseEntity.ok(message);
    }

    // 获取与特定用户的聊天记录
    @GetMapping("/chat/{userId}")
    public ResponseEntity<List<Message>> getChatHistory(
            @PathVariable Integer userId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        List<Message> messages = messageService.getChatHistory(currentUser.getId(), userId);
        return ResponseEntity.ok(messages);
    }

    // 获取所有消息列表（包括发送和接收的）
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages(
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        List<Message> messages = messageService.getAllMessages(currentUser.getId());
        return ResponseEntity.ok(messages);
    }

    // 获取未读消息数量
    @GetMapping("/unread/count")
    public ResponseEntity<Integer> getUnreadCount(
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        int count = messageService.getUnreadCount(currentUser.getId());
        return ResponseEntity.ok(count);
    }

    // 标记消息为已读
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Integer messageId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        messageService.markAsRead(messageId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // 删除消息
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Integer messageId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        messageService.deleteMessage(messageId, currentUser.getId());
        return ResponseEntity.ok().build();
    }
}