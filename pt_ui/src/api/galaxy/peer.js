import request from '@/utils/request'

// 查询节点管理列表
export function listPeer(query) {
  return request({
    url: '/galaxy/peer/list',
    method: 'get',
    params: query
  })
}

// 查询节点管理详细
export function getPeer(id) {
  return request({
    url: '/galaxy/peer/' + id,
    method: 'get'
  })
}

// 新增节点管理
export function addPeer(data) {
  return request({
    url: '/galaxy/peer',
    method: 'post',
    data: data
  })
}

// 修改节点管理
export function updatePeer(data) {
  return request({
    url: '/galaxy/peer',
    method: 'put',
    data: data
  })
}

// 删除节点管理
export function delPeer(id) {
  return request({
    url: '/galaxy/peer/' + id,
    method: 'delete'
  })
}

// 导出节点管理
export function exportPeer(query) {
  return request({
    url: '/galaxy/peer/export',
    method: 'get',
    params: query
  })
}