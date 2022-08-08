import request from '@/utils/request'

// 查询利息列表
export function listIntersest(query) {
  return request({
    url: '/tron/intersest/list',
    method: 'get',
    params: query
  })
}

// 查询利息详细
export function getIntersest(id) {
  return request({
    url: '/tron/intersest/' + id,
    method: 'get'
  })
}

// 新增利息
export function addIntersest(data) {
  return request({
    url: '/tron/intersest',
    method: 'post',
    data: data
  })
}

// 修改利息
export function updateIntersest(data) {
  return request({
    url: '/tron/intersest',
    method: 'put',
    data: data
  })
}

// 删除利息
export function delIntersest(id) {
  return request({
    url: '/tron/intersest/' + id,
    method: 'delete'
  })
}

// 导出利息
export function exportIntersest(query) {
  return request({
    url: '/tron/intersest/export',
    method: 'get',
    params: query
  })
}