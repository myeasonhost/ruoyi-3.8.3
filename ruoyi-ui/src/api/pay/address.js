import request from '@/utils/request'

// 查询收款地址列表
export function listAddress(query) {
  return request({
    url: '/pay/address/list',
    method: 'get',
    params: query
  })
}

// 查询收款地址详细
export function getAddress(id, method) {
  return request({
    url: '/pay/address/' + id + '/' + method,
    method: 'get'
  })
}

// 新增收款地址
export function addAddress(data) {
  return request({
    url: '/pay/address',
    method: 'post',
    data: data
  })
}

// 修改收款地址
export function updateAddress(data) {
  return request({
    url: '/pay/address',
    method: 'put',
    data: data
  })
}

// 修改收款状态地址
export function changeStatus(id, status) {
  return request({
    url: '/pay/address/' + id + '/' + status,
    method: 'put'
  })
}

// 删除收款地址
export function delAddress(id) {
  return request({
    url: '/pay/address/' + id,
    method: 'delete'
  })
}
