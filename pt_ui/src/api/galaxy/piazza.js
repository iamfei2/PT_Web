import request from '@/utils/request'

export function listTorrents(query) {
  return request({
    url: '/piazza/list',
    method: 'get',
    params: query
  })
}

export function getTorrent(id) {
  return request({
    url: '/piazza/' + id,
    method: 'get'
  })
}

export function getPaymentInfo(id) {
  return request({
    url: '/piazza/payment/info?torrentId=' + id,
    method: 'get'
  })
}

export function getPaymentConfirm(id) {
  return request({
    url: '/piazza/payment/confirm?torrentId=' + id,
    method: 'get'
  })
}

export function getDownload(id) {
  return request({
    url: '/piazza/download/' + id,
    method: 'get'
  })
}

export function getAttachmentDownload(id) {
  return request({
    url: '/piazza/download/attachment/' + id,
    method: 'get'
  })
}

export function getNotice(query) {
  return request({
    url: '/piazza/notice',
    method: 'get',
    params: query
  })
}

export function getReward() {
  return request({
    url: '/piazza/reward',
    method: 'get'
  })
}
