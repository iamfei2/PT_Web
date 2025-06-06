import request from '@/utils/request'

// 查询小黑屋列表
export function listBanned(query) {
  return request({
    url: '/galaxy/banned/list',
    method: 'get',
    params: query
  })
}

// 查询小黑屋详细
export function getBanned(id) {
  return request({
    url: '/galaxy/banned/' + id,
    method: 'get'
  })
}

// 新增小黑屋
export function addBanned(data) {
  return request({
    url: '/galaxy/banned',
    method: 'post',
    data: data
  })
}

// 修改小黑屋
export function updateBanned(data) {
  return request({
    url: '/galaxy/banned',
    method: 'put',
    data: data
  })
}

// 删除小黑屋
export function delBanned(id) {
  return request({
    url: '/galaxy/banned/' + id,
    method: 'delete'
  })
}

// 导出小黑屋
export function exportBanned(query) {
  return request({
    url: '/galaxy/banned/export',
    method: 'get',
    params: query
  })
}

// 提交申诉信息
export function createAppeal(data) {
  return request({
    url: '/galaxy/banned/appeal/create',
    method: 'post',
    data: data
  })
}

// 审批申诉信息
export function appealAudit(data) {
  return request({
    url: '/galaxy/banned/appeal/audit',
    method: 'post',
    data: data
  })
}

