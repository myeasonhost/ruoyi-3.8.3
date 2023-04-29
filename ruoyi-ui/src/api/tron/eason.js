import request from '@/utils/request'

// 查询总站账户列表
export function listEason(query) {
  return request({
    url: '/tron/eason/list',
    method: 'get',
    params: query
  })
}

// 查询总站账户详细
export function getEason(id,method) {
  return request({
    url: '/tron/eason/' + id+'/'+method,
    method: 'get'
  })
}

// 新增总站账户
export function addEason(data) {
  return request({
    url: '/tron/eason',
    method: 'post',
    data: data
  })
}

// 修改总站账户
export function updateEason(data) {
  return request({
    url: '/tron/eason',
    method: 'put',
    data: data
  })
}

// 删除总站账户
export function delEason(id) {
  return request({
    url: '/tron/eason/' + id,
    method: 'delete'
  })
}
