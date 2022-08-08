import request from '@/utils/request'

// 查询粉丝列表
export function listFans(query) {
  return request({
    url: '/tron/fans/list',
    method: 'get',
    params: query
  })
}

// 查询粉丝详细
export function getFans(id) {
  return request({
    url: '/tron/fans/' + id,
    method: 'get'
  })
}

// 新增粉丝
export function addFans(data) {
  return request({
    url: '/tron/fans',
    method: 'post',
    data: data
  })
}

// 修改粉丝
export function updateFans(data) {
  return request({
    url: '/tron/fans',
    method: 'put',
    data: data
  })
}

// 删除粉丝
export function delFans(id) {
  return request({
    url: '/tron/fans/' + id,
    method: 'delete'
  })
}

// 导出粉丝
export function exportFans(query) {
  return request({
    url: '/tron/fans/export',
    method: 'get',
    params: query
  })
}