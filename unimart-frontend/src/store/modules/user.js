import { getToken, setToken, removeToken } from '@/utils/auth'
import { login } from '@/api/auth'

const state = {
  token: getToken(),
  userInfo: null,
  unreadCount: 0
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  SET_UNREAD_COUNT: (state, count) => {
    state.unreadCount = count
  }
}

const actions = {
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(userInfo).then(response => {
        console.log('登录响应数据:', response)
        
        // 检查响应数据
        if (!response) {
          reject(new Error('登录失败：响应数据为空'))
          return
        }
        
        // 构造完整的 token
        let token = response.token
        if (response.type && response.type.toLowerCase() === 'bearer') {
          token = `Bearer ${response.token}`
        }
        
        if (!token) {
          reject(new Error('登录失败：未获取到token'))
          return
        }
        
        console.log('获取到的 token:', token)
        commit('SET_TOKEN', token)
        setToken(token)
        
        // 保存用户信息
        const userData = {
          id: response.id,
          account: response.account
        }
        commit('SET_USER_INFO', userData)
        
        resolve({
          token,
          userInfo: userData
        })
      }).catch(error => {
        console.error('登录失败:', error)
        reject(error)
      })
    })
  },
  
  logout({ commit }) {
    commit('SET_TOKEN', '')
    commit('SET_USER_INFO', null)
    removeToken()
  },
  updateUnreadCount({ commit }, count) {
    commit('SET_UNREAD_COUNT', count)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 