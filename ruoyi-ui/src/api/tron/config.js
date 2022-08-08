import request from '@/utils/request'

// 查询矿机设置列表
export function listConfig(query) {
  return request({
    url: '/tron/config/list',
    method: 'get',
    params: query
  })
}

// 查询矿机设置详细
export function getConfig(id) {
  return request({
    url: '/tron/config/' + id,
    method: 'get'
  })
}

// 新增矿机设置
export function addConfig(data) {
  return request({
    url: '/tron/config',
    method: 'post',
    data: data
  })
}

// 修改矿机设置
export function updateConfig(data) {
  return request({
    url: '/tron/config',
    method: 'put',
    data: data
  })
}

// 删除矿机设置
export function delConfig(id) {
  return request({
    url: '/tron/config/' + id,
    method: 'delete'
  })
}

// 导出矿机设置
export function exportConfig(query) {
  return request({
    url: '/tron/config/export',
    method: 'get',
    params: query
  })
}