import request from '@/utils/request'

// 查询资源广场列表
export function listTorrent(query) {
  return request({
    url: '/galaxy/torrent/list',
    method: 'get',
    params: query
  })
}

// 查询资源广场列表
export function listReviewTorrent(query) {
  return request({
    url: '/galaxy/torrent/review/list',
    method: 'get',
    params: query
  })
}

// 查询资源广场详细
export function getTorrent(id) {
  return request({
    url: '/galaxy/torrent/' + id,
    method: 'get'
  })
}

export function getVerify(torrentId, verify, msg) {
  return request({
    url: '/galaxy/torrent/verify?torrentId=' + torrentId + '&verify=' + verify + '&msg=' + msg,
    method: 'get'
  })
}

// 新增资源广场
export function addTorrent(data) {
  return request({
    url: '/galaxy/torrent',
    method: 'post',
    data: data
  })
}

// 修改资源广场
export function updateTorrent(data) {
  return request({
    url: '/galaxy/torrent',
    method: 'put',
    data: data
  })
}

// 删除资源广场
export function delTorrent(id) {
  return request({
    url: '/galaxy/torrent/' + id,
    method: 'delete'
  })
}

// 导出资源广场
export function exportTorrent(query) {
  return request({
    url: '/galaxy/torrent/export',
    method: 'get',
    params: query
  })
}
