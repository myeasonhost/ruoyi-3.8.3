import request from '@/utils/request'

// 查询图片配置01列表
export function listConfig01(query) {
  return request({
    url: '/tron/config01/list',
    method: 'get',
    params: query
  })
}

// 查询图片配置01详细
export function getConfig01(id) {
  return request({
    url: '/tron/config01/' + id,
    method: 'get'
  })
}

// 新增图片配置01
export function addConfig01(data) {
  return request({
    url: '/tron/config01',
    method: 'post',
    data: data
  })
}

// 修改图片配置01
export function updateConfig01(data) {
  return request({
    url: '/tron/config01',
    method: 'put',
    data: data
  })
}

// 删除图片配置01
export function delConfig01(id) {
  return request({
    url: '/tron/config01/' + id,
    method: 'delete'
  })
}

// 导出图片配置01
export function exportConfig01(query) {
  return request({
    url: '/tron/config01/export',
    method: 'get',
    params: query
  })
}