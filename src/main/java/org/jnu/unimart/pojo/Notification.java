package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;
    @Column(name = "sender_id")
    private int senderId; // 发送者id
    @Column(name = "accepter_id")
    private int accepterId; //接收者id
    @Column(name = "notification_content")
    private String notificationContent; //消息内容
    @Column(name = "notification_time")
    private LocalDateTime notificationTime; //消息时间
}
