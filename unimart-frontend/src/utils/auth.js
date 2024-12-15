const TokenKey = 'Admin-Token'

export function getToken() {
  let token = localStorage.getItem(TokenKey)
  console.log('从 localStorage 获取的 token:', token)
  if (!token) {
    return null
  }
  return token
}

export function setToken(token) {
  if (!token) {
    console.warn('尝试保存无效的 token:', token)
    return
  }
  console.log('保存 token:', token)
  localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return localStorage.removeItem(TokenKey)
}

// 获取用户信息
export function getUserInfo() {
  const userInfo = localStorage.getItem('user_info')
  return userInfo ? JSON.parse(userInfo) : null
}

// 保存用户信息
export function setUserInfo(userInfo) {
  localStorage.setItem('user_info', JSON.stringify(userInfo))
}

// 移除用户信息
export function removeUserInfo() {
  localStorage.removeItem('user_info')
} 