import request from '@/utils/request'

// 查询帖子列表
export function listForumPost(query) {
  return request({
    url: '/forum/post/list',
    method: 'get',
    params: query
  })
}

// 查询帖子详细
export function getForumPost(postId) {
  return request({
    url: `/forum/post/${postId}`,
    method: 'get'
  })
}

// 新增帖子
export function addForumPost(data) {
  return request({
    url: '/forum/post',
    method: 'post',
    data: data
  })
}

// 修改帖子
export function updateForumPost(data) {
  return request({
    url: '/forum/post',
    method: 'put',
    data: data
  })
}

// 删除帖子
export function delForumPost(postId) {
  return request({
    url: `/forum/post/${postId}`,
    method: 'delete'
  })
}
