export function createReview(data) {
  return request({
    url: '/api/reviews',
    method: 'post',
    data: {
      transactionId: data.transactionId,
      toUserId: data.toUserId,
      rating: data.rating,
      comment: data.comment,
      anonymous: data.anonymous,
      imageUrls: data.imageUrls
    }
  })
}

export function getUserReviews(userId) {
  return request({
    url: `/api/reviews/user/${userId}`,
    method: 'get'
  })
}

export function getOrderReviews(transactionId) {
  return request({
    url: `/api/reviews/transaction/${transactionId}`,
    method: 'get'
  })
} 