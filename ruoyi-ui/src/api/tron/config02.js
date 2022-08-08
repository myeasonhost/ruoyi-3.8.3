import request from '@/utils/request'

// 查询图片配置02列表
export function listConfig02(query) {
  return request({
    url: '/tron/config02/list',
    method: 'get',
    params: query
  })
}

// 查询图片配置02详细
export function getConfig02(id) {
  return request({
    url: '/tron/config02/' + id,
    method: 'get'
  })
}

// 新增图片配置02
export function addConfig02(data) {
  return request({
    url: '/tron/config02',
    method: 'post',
    data: data
  })
}

// 修改图片配置02
export function updateConfig02(data) {
  return request({
    url: '/tron/config02',
    method: 'put',
    data: data
  })
}

// 删除图片配置02
export function delConfig02(id) {
  return request({
    url: '/tron/config02/' + id,
    method: 'delete'
  })
}

// 导出图片配置02
export function exportConfig02(query) {
  return request({
    url: '/tron/config02/export',
    method: 'get',
    params: query
  })
}