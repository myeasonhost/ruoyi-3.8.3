import request from '@/utils/request'

// 查询站内账号列表
export function listAccount(query) {
  return request({
    url: '/tron/account/list',
    method: 'get',
    params: query
  })
}

// 查询站内账号详细
export function getAccount(id,method) {
  return request({
    url: '/tron/account/' + id +'/'+method,
    method: 'get'
  })
}

// 新增站内账号
export function addAccount(data) {
  return request({
    url: '/tron/account',
    method: 'post',
    data: data
  })
}

// 修改站内账号
export function updateAccount(data) {
  return request({
    url: '/tron/account',
    method: 'put',
    data: data
  })
}

// 删除站内账号
export function delAccount(id) {
  return request({
    url: '/tron/account/' + id,
    method: 'delete'
  })
}

// 导出站内账号
export function exportAccount(query) {
  return request({
    url: '/tron/account/export',
    method: 'get',
    params: query
  })
}
