import request from '@/utils/request'

// 查询邀请注册列表
export function listInvite(query) {
  return request({
    url: '/galaxy/invite/list',
    method: 'get',
    params: query
  })
}

// 购买邀请码
export function confirmPurchase() {
  return request({
    url: '/galaxy/invite/purchase',
    method: 'post'
  })
}

// 获取支付信息
export function getPurchase() {
  return request({
    url: '/galaxy/invite/purchase',
    method: 'get'
  })
}

// 查询邀请注册详细
export function getInvite(id) {
  return request({
    url: '/galaxy/invite/' + id,
    method: 'get'
  })
}

// 新增邀请注册
export function addInvite(data) {
  return request({
    url: '/galaxy/invite',
    method: 'post',
    data: data
  })
}

// 修改邀请注册
export function updateInvite(data) {
  return request({
    url: '/galaxy/invite',
    method: 'put',
    data: data
  })
}

// 删除邀请注册
export function delInvite(id) {
  return request({
    url: '/galaxy/invite/' + id,
    method: 'delete'
  })
}

// 导出邀请注册
export function exportInvite(query) {
  return request({
    url: '/galaxy/invite/export',
    method: 'get',
    params: query
  })
}
