import request from '@/utils/request'

// 查询分享类别列表
export function listCategory(query) {
  return request({
    url: '/galaxy/category/list',
    method: 'get',
    params: query
  })
}

// 查询分享类别详细
export function getCategory(id) {
  return request({
    url: '/galaxy/category/' + id,
    method: 'get'
  })
}

// 新增分享类别
export function addCategory(data) {
  return request({
    url: '/galaxy/category',
    method: 'post',
    data: data
  })
}

// 修改分享类别
export function updateCategory(data) {
  return request({
    url: '/galaxy/category',
    method: 'put',
    data: data
  })
}

// 删除分享类别
export function delCategory(id) {
  return request({
    url: '/galaxy/category/' + id,
    method: 'delete'
  })
}

// 导出分享类别
export function exportCategory(query) {
  return request({
    url: '/galaxy/category/export',
    method: 'get',
    params: query
  })
}