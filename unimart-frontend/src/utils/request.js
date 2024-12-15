import axios from 'axios'
import { getToken } from '@/utils/auth'
import router from '@/router'
import store from '@/store'
// 创建 axios 实例
const service = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true,
})
// 添加基础URL
export const BASE_URL = 'http://localhost:8080'  // 或者你的后端服务器地址
// 请求拦截器
service.interceptors.request.use(
  config => {
    console.log('请求配置:', config)
    const token = getToken()
    console.log('当前 token:', token)
    
    if (token) {
      console.log('发送请求:', {
        url: config.url,
        method: config.method,
        token: token,
        headers: config.headers
      })
      config.headers['Authorization'] = token
    } else {
      if (!config.url.includes('/login') && !config.url.includes('/register')) {
        console.warn('未找到有效token，重定向到登录页')
        router.push('/login')
        return Promise.reject('请先登录')
      }
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)
// 响应拦截器
service.interceptors.response.use(
  response => {
    console.log('Response Data:', response.data)
    return response.data
  },
  error => {
    console.error('Response Error:', {
      status: error.response?.status,
      data: error.response?.data,
      config: error.config,
      message: error.message,
      url: error.config?.url,
      headers: error.config?.headers
    })

    const errorMessage = error.response?.data?.message || error.message || '未知错误'

    if (error.response) {
      const { status } = error.response
      
      switch (status) {
        case 400:
          console.error('请求参数错误:', errorMessage)
          break
        case 401:
          console.error('未授权:', errorMessage)
          store.dispatch('user/resetToken')
          router.push('/login')
          break
        case 403:
          console.error('禁止访问:', errorMessage)
          router.push('/403')
          break
        case 404:
          console.error('资源不存在:', errorMessage)
          router.push('/404')
          break
        case 500:
          console.error('服务器错误:', errorMessage)
          break
        default:
          console.error('其他错误:', errorMessage)
      }
    } else if (error.request) {
      console.error('未收到响应:', error.request)
    } else {
      console.error('请求配置错误:', error.message)
    }

    return Promise.reject({
      message: errorMessage,
      status: error.response?.status,
      data: error.response?.data,
      originalError: error
    })
  }
)

export default service 