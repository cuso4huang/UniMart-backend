package org.jnu.unimart.pojo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
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

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getAccepterId() {
        return accepterId;
    }

    public void setAccepterId(int accepterId) {
        this.accepterId = accepterId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", senderId=" + senderId +
                ", accepterId=" + accepterId +
                ", notificationContent='" + notificationContent + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }
}
