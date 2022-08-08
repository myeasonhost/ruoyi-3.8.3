import request from '@/utils/request'

// 查询结算记录列表
export function listBill(query) {
  return request({
    url: '/tron/bill/list',
    method: 'get',
    params: query
  })
}

// 查询结算记录详细
export function getBill(id) {
  return request({
    url: '/tron/bill/' + id,
    method: 'get'
  })
}

// 新增结算记录
export function addBill(data) {
  return request({
    url: '/tron/bill',
    method: 'post',
    data: data
  })
}

// 修改结算记录
export function updateBill(data) {
  return request({
    url: '/tron/bill',
    method: 'put',
    data: data
  })
}

// 删除结算记录
export function delBill(id) {
  return request({
    url: '/tron/bill/' + id,
    method: 'delete'
  })
}

// 导出结算记录
export function exportBill(query) {
  return request({
    url: '/tron/bill/export',
    method: 'get',
    params: query
  })
}