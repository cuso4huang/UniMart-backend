export function createReport(data) {
  return request({
    url: '/api/reports',
    method: 'post',
    data: {
      accusedUserId: data.accusedUserId,
      transactionId: data.transactionId,
      reason: data.reason,
      imageUrls: data.imageUrls
    }
  })
}

export function getAllReports() {
  return request({
    url: '/api/reports',
    method: 'get'
  })
}

export function getReportById(id) {
  return request({
    url: `/api/reports/${id}`,
    method: 'get'
  })
} 