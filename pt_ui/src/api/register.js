import request from '@/utils/request'

// 登录方法
export function register(userData) {
  return request({
    url: '/register',
    method: 'post',
    data: userData
  })
}
