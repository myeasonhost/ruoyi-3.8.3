import request from '@/utils/request'

// 查询授权记录列表
export function listRecord(query) {
  return request({
    url: '/tron/record/list',
    method: 'get',
    params: query
  })
}

// 导出授权记录
export function exportRecord(query) {
  return request({
    url: '/tron/record/export',
    method: 'get',
    params: query
  })
}
