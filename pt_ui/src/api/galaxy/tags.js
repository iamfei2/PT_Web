import request from '@/utils/request'

// 查询种子标签列表
export function listTags(query) {
  return request({
    url: '/galaxy/tags/list',
    method: 'get',
    params: query
  })
}

// 查询种子标签详细
export function getTags(id) {
  return request({
    url: '/galaxy/tags/' + id,
    method: 'get'
  })
}

// 新增种子标签
export function addTags(data) {
  return request({
    url: '/galaxy/tags',
    method: 'post',
    data: data
  })
}

// 修改种子标签
export function updateTags(data) {
  return request({
    url: '/galaxy/tags',
    method: 'put',
    data: data
  })
}

// 删除种子标签
export function delTags(id) {
  return request({
    url: '/galaxy/tags/' + id,
    method: 'delete'
  })
}

// 导出种子标签
export function exportTags(query) {
  return request({
    url: '/galaxy/tags/export',
    method: 'get',
    params: query
  })
}