import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function updateUserInfo(data) {
  console.log('API调用数据:', data)
  return request({
    url: '/user/update',
    method: 'put',
    data: {
      username: data.username || undefined,
      account: data.account || undefined
    }
  })
}

export function getAllUsers() {
  return request({
    url: '/users',
    method: 'get'
  })
}

export function getUserById(id) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  })
}

export function searchUser(username) {
  return request({
    url: '/users/search',
    method: 'get',
    params: { username }
  })
}

export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

export function changePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data: {
      oldPassword: data.oldPassword,
      newPassword: data.newPassword
    }
  })
}

export function deleteAccount() {
  return request({
    url: '/user/delete',
    method: 'delete'
  })
}

export function getMyProducts() {
  return request({
    url: '/products/my',
    method: 'get'
  })
}
  