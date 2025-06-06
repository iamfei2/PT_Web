import request from '@/utils/request'

// 查询购买记录列表
export function listTorrent_purchase(query) {
  return request({
    url: '/galaxy/torrent_purchase/list',
    method: 'get',
    params: query
  })
}

// 查询购买记录详细
export function getTorrent_purchase(id) {
  return request({
    url: '/galaxy/torrent_purchase/' + id,
    method: 'get'
  })
}

// 新增购买记录
export function addTorrent_purchase(data) {
  return request({
    url: '/galaxy/torrent_purchase',
    method: 'post',
    data: data
  })
}

// 修改购买记录
export function updateTorrent_purchase(data) {
  return request({
    url: '/galaxy/torrent_purchase',
    method: 'put',
    data: data
  })
}

// 删除购买记录
export function delTorrent_purchase(id) {
  return request({
    url: '/galaxy/torrent_purchase/' + id,
    method: 'delete'
  })
}

// 导出购买记录
export function exportTorrent_purchase(query) {
  return request({
    url: '/galaxy/torrent_purchase/export',
    method: 'get',
    params: query
  })
}