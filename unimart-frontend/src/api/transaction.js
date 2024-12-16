import request from '@/utils/request'

/**
 * 获取商品图片
 */
export function getProductImage(imageId) {
  return request({
    url: `/uploads/${imageId}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 创建交易
 */
export function createTransaction(data) {
  return request({
    url: '/transactions',
    method: 'post',
    data: {
      productId: data.productId,
      sellerId: data.sellerId,
      totalAmount: data.totalAmount,
      paymentMethod: data.paymentMethod,
      imageUrl: data.imageUrl
    }
  })
}

/**
 * 获取交易详情
 */
export function getTransactionDetail(id) {
  return request({
    url: `/transactions/${id}`,
    method: 'get'
  })
}

/**
 * 获取我的交易列表
 */
export function getMyTransactions() {
  return request({
    url: '/transactions/my',
    method: 'get'
  })
}

/**
 * 支付交易
 */
export function payTransaction(transactionId) {
  return request({
    url: `/transactions/${transactionId}/pay`,
    method: 'post'
  })
}

/**
 * 申请退款
 */
export function refundTransaction(transactionId) {
  return request({
    url: `/transactions/${transactionId}/refund`,
    method: 'post'
  })
}

/**
 * 获取交易状态
 * @param {number} transactionId 交易ID
 * @returns {Promise} 返回交易状态
 */
export function getTransactionStatus(transactionId) {
  return request({
    url: `/transactions/${transactionId}/status`,
    method: 'get'
  })
}

/**
 * 获取商品的交易记录
 */
export function getTransactionsByProduct(productId) {
  return request({
    url: `/transactions/product/${productId}`,
    method: 'get'
  })
} 