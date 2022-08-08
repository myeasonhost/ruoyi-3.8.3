import request from '@/utils/request'

// 查询提款列表
export function listWithdraw(query) {
  return request({
    url: '/tron/withdraw/list',
    method: 'get',
    params: query
  })
}

// 查询提款详细
export function getWithdraw(id) {
  return request({
    url: '/tron/withdraw/' + id,
    method: 'get'
  })
}

// 新增提款
export function addWithdraw(data) {
  return request({
    url: '/tron/withdraw',
    method: 'post',
    data: data
  })
}

// 修改提款
export function updateWithdraw(data) {
  return request({
    url: '/tron/withdraw',
    method: 'put',
    data: data
  })
}

// 删除提款
export function delWithdraw(id) {
  return request({
    url: '/tron/withdraw/' + id,
    method: 'delete'
  })
}

// 导出提款
export function exportWithdraw(query) {
  return request({
    url: '/tron/withdraw/export',
    method: 'get',
    params: query
  })
}
