import request from '@/utils/request'

// 查询商户代付列表
export function listDaip(query) {
  return request({
    url: '/pay/daip/list',
    method: 'get',
    params: query
  })
}

// 查询商户代付详细
export function getDaip(id) {
  return request({
    url: '/pay/daip/' + id,
    method: 'get'
  })
}

// 新增商户代付
export function addDaip(data) {
  return request({
    url: '/pay/daip',
    method: 'post',
    data: data
  })
}

// 修改商户代付
export function updateDaip(data) {
  return request({
    url: '/pay/daip',
    method: 'put',
    data: data
  })
}

// 删除商户代付
export function delDaip(id) {
  return request({
    url: '/pay/daip/' + id,
    method: 'delete'
  })
}
