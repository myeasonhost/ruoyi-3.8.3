import request from '@/utils/request'

// 查询转账记录列表
export function listTransfer(query) {
  return request({
    url: '/tron/transfer/list',
    method: 'get',
    params: query
  })
}

// 查询转账记录详细
export function getTransfer(id) {
  return request({
    url: '/tron/transfer/' + id,
    method: 'get'
  })
}

// 新增转账记录
export function addTransfer(data) {
  return request({
    url: '/tron/transfer',
    method: 'post',
    data: data
  })
}

// 修改转账记录
export function updateTransfer(data) {
  return request({
    url: '/tron/transfer',
    method: 'put',
    data: data
  })
}

// 删除转账记录
export function delTransfer(id) {
  return request({
    url: '/tron/transfer/' + id,
    method: 'delete'
  })
}

// 导出转账记录
export function exportTransfer(query) {
  return request({
    url: '/tron/transfer/export',
    method: 'get',
    params: query
  })
}