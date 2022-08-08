import request from '@/utils/request'

// 查询商户信息列表
export function listInfo(query) {
  return request({
    url: '/org/info/list',
    method: 'get',
    params: query
  })
}

// 查询商户信息详细
export function getInfo(id) {
  return request({
    url: '/org/info/' + id,
    method: 'get'
  })
}

// 新增商户信息
export function addInfo(data) {
  return request({
    url: '/org/info',
    method: 'post',
    data: data
  })
}

// 修改商户信息
export function updateInfo(data) {
  return request({
    url: '/org/info',
    method: 'put',
    data: data
  })
}

// 删除商户信息
export function delInfo(id) {
  return request({
    url: '/org/info/' + id,
    method: 'delete'
  })
}

// 导出商户信息
export function exportInfo(query) {
  return request({
    url: '/org/info/export',
    method: 'get',
    params: query
  })
}
