import request from '@/utils/request'

// 查询商户账户列表
export function listAccount(query) {
  return request({
    url: '/org/account/list',
    method: 'get',
    params: query
  })
}

// 查询商户账户详细
export function getAccount(id) {
  return request({
    url: '/org/account/' + id,
    method: 'get'
  })
}

// 新增商户账户
export function addAccount(data) {
  return request({
    url: '/org/account',
    method: 'post',
    data: data
  })
}

// 修改商户账户
export function updateAccount(data) {
  return request({
    url: '/org/account',
    method: 'put',
    data: data
  })
}

// 删除商户账户
export function delAccount(id) {
  return request({
    url: '/org/account/' + id,
    method: 'delete'
  })
}

// 导出商户账户
export function exportAccount(query) {
  return request({
    url: '/org/account/export',
    method: 'get',
    params: query
  })
}
