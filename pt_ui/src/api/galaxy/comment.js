import request from '@/utils/request'

// 查询评论列表
export function listComments(query) {
  return request({
    url: '/forum/comment/list',
    method: 'get',
    params: query
  })
}

// 新增评论
export function addComment(data) {
  return request({
    url: '/forum/comment',
    method: 'post',
    data: data
  })
}

// 新增回复
export function addReply(data) {
  return request({
    url: '/forum/comment/reply',
    method: 'post',
    data: data
  })
}

// 删除评论
export function delComment(commentId) {
  return request({
    url: '/forum/comment/' + commentId,
    method: 'delete'
  })
}
