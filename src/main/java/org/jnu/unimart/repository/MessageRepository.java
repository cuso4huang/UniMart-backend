package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // 获取两个用户之间的所有消息
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreateTimeDesc(
            Integer senderId1, Integer receiverId1,
            Integer senderId2, Integer receiverId2
    );

    // 获取用户发送或接收的所有消息
    List<Message> findBySenderIdOrReceiverIdOrderByCreateTimeDesc(
            Integer senderId, Integer receiverId
    );

    // 获取用户接收的所有消息
    List<Message> findByReceiverIdOrderByCreateTimeDesc(Integer receiverId);

    // 获取用户未读消息数量
    int countByReceiverIdAndIsReadFalse(Integer receiverId);
}