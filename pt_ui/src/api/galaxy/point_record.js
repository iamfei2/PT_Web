import request from '@/utils/request'

// 查询积分明细列表
export function listPoint_record(query) {
  return request({
    url: '/galaxy/point_record/list',
    method: 'get',
    params: query
  })
}

// 查询积分明细详细
export function getPoint_record(id) {
  return request({
    url: '/galaxy/point_record/' + id,
    method: 'get'
  })
}

// 新增积分明细
export function addPoint_record(data) {
  return request({
    url: '/galaxy/point_record',
    method: 'post',
    data: data
  })
}

// 修改积分明细
export function updatePoint_record(data) {
  return request({
    url: '/galaxy/point_record',
    method: 'put',
    data: data
  })
}

// 删除积分明细
export function delPoint_record(id) {
  return request({
    url: '/galaxy/point_record/' + id,
    method: 'delete'
  })
}

// 导出积分明细
export function exportPoint_record(query) {
  return request({
    url: '/galaxy/point_record/export',
    method: 'get',
    params: query
  })
}