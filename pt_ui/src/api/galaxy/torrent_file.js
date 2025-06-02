import request from '@/utils/request'

// 查询种子文件列表
export function listTorrent_file(query) {
  return request({
    url: '/galaxy/torrent_file/list',
    method: 'get',
    params: query
  })
}

// 查询种子文件详细
export function getTorrent_file(id) {
  return request({
    url: '/galaxy/torrent_file/' + id,
    method: 'get'
  })
}

// 新增种子文件
export function addTorrent_file(data) {
  return request({
    url: '/galaxy/torrent_file',
    method: 'post',
    data: data
  })
}

// 修改种子文件
export function updateTorrent_file(data) {
  return request({
    url: '/galaxy/torrent_file',
    method: 'put',
    data: data
  })
}

// 删除种子文件
export function delTorrent_file(id) {
  return request({
    url: '/galaxy/torrent_file/' + id,
    method: 'delete'
  })
}

// 导出种子文件
export function exportTorrent_file(query) {
  return request({
    url: '/galaxy/torrent_file/export',
    method: 'get',
    params: query
  })
}