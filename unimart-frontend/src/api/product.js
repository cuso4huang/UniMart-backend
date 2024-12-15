import request from '@/utils/request'

/**
 * 获取商品列表
 */
export function getProductList() {
  return request({
    url: '/products',
    method: 'get'
  })
}

/**
 * 根据分类获取商品列表
 * @param {number} categoryId 分类ID
 */
export function getProductsByCategory(categoryId) {
  return request({
    url: `/products/category/${categoryId}`,
    method: 'get'
  })
}

/**
 * 搜索商品
 * @param {string} keyword 搜索关键词
 */
export function searchProducts(keyword) {
  return request({
    url: '/products/search',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 获取商品详情
 * @param {number} id 商品ID
 */
export function getProductDetail(id) {
  return request({
    url: `/products/${id}`,
    method: 'get'
  })
}

/**
 * 创建商品
 * @param {Object} data 商品信息
 */
export function createProduct(data) {
  return request({
    url: '/products',
    method: 'post',
    data
  })
}

/**
 * 更新商品
 * @param {number} id 商品ID
 * @param {Object} data 更新的商品信息
 */
export function updateProduct(id, data) {
  return request({
    url: `/products/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除商品
 * @param {number} id 商品ID
 */
export function deleteProduct(id) {
  return request({
    url: `/products/${id}`,
    method: 'delete'
  })
}

/**
 * 获取当前用户的商品列表
 */
export function getMyProducts() {
  return request({
    url: '/products/my',
    method: 'get'
  })
}

/**
 * 发布商品
 * @param {Object} data 商品信息
 */
export function publishProduct(data) {
  return request({
    url: '/products',
    method: 'post',
    data
  })
}

/**
 * 上传图片
 * @param {File} file 图片文件
 */
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
} 