import request from '@/utils/request'
import { removeToken, removeUserInfo } from '@/utils/auth'

/**
 * 用户登录
 * @param {Object} data - 登录信息
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise} 返回包含token和用户信息的响应
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 用户注册
 * @param {Object} data - 注册信息
 * @param {string} data.account - 账号
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise} 返回注册结果
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data: {
      username: data.username,
      password: data.password,
      account: data.account
    }
  })
}

/**
 * 退出登录
 */
export function logout() {
  return new Promise(resolve => {
    removeToken()
    removeUserInfo()
    resolve()
  })
} 