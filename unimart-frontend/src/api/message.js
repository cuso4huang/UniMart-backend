import request from '@/utils/request'

/**
 * 发送消息给卖家
 */
export function sendMessage(data) {
  return request({
    url: '/messages/send',
    method: 'post',
    data
  })
}

/**
 * 获取消息列表
 */
export function getMessages() {
  return request({
    url: '/messages',
    method: 'get'
  })
}

/**
 * 获取与特定用户的聊天记录
 */
export function getChatHistory(userId) {
  return request({
    url: `/messages/chat/${userId}`,
    method: 'get'
  })
}

/**
 * 获取未读消息数量
 */
export function getUnreadCount() {
  return request({
    url: '/messages/unread/count',
    method: 'get'
  })
}

/**
 * 标记消息为已读
 */
export function markAsRead(messageId) {
  return request({
    url: `/messages/${messageId}/read`,
    method: 'put'
  })
} 