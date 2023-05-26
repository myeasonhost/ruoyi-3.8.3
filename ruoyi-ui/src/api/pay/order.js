import request from '@/utils/request'

// 查询支付订单列表
export function listOrder(query) {
  return request({
    url: '/pay/order/list',
    method: 'get',
    params: query
  })
}

// 查询支付订单详细
export function getOrder(id) {
  return request({
    url: '/pay/order/' + id,
    method: 'get'
  })
}

// 新增支付订单
export function addOrder(data) {
  return request({
    url: '/pay/order',
    method: 'post',
    data: data
  })
}

// 修改支付订单
export function updateOrder(data) {
  return request({
    url: '/pay/order',
    method: 'put',
    data: data
  })
}

// 删除支付订单
export function delOrder(id) {
  return request({
    url: '/pay/order/' + id,
    method: 'delete'
  })
}

// 查询入U统计
export function countOrder() {
  return request({
    url: '/pay/order/count/stat',
    method: 'post'
  })
}
