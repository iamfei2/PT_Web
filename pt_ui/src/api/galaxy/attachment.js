import request from '@/utils/request'

// 查询种子附件列表
export function listAttachment(query) {
  return request({
    url: '/galaxy/attachment/list',
    method: 'get',
    params: query
  })
}

// 查询种子附件详细
export function getAttachment(id) {
  return request({
    url: '/galaxy/attachment/' + id,
    method: 'get'
  })
}

// 新增种子附件
export function addAttachment(data) {
  return request({
    url: '/galaxy/attachment',
    method: 'post',
    data: data
  })
}

// 修改种子附件
export function updateAttachment(data) {
  return request({
    url: '/galaxy/attachment',
    method: 'put',
    data: data
  })
}

// 删除种子附件
export function delAttachment(id) {
  return request({
    url: '/galaxy/attachment/' + id,
    method: 'delete'
  })
}

// 导出种子附件
export function exportAttachment(query) {
  return request({
    url: '/galaxy/attachment/export',
    method: 'get',
    params: query
  })
}