import request from '@/utils/request'

// 查询鱼苗管理列表
export function listFish(query) {
  return request({
    url: '/tron/fish/list',
    method: 'get',
    params: query
  })
}

// 查询鱼苗统计
export function countFish() {
  return request({
    url: '/tron/fish/count/stat',
    method: 'post'
  })
}

// 查询账单统计
export function countBill(id,method) {
  return request({
    url: '/tron/bill/count/'+method,
    method: 'post'
  })
}

// 查询鱼苗管理详细
export function getFish(id,method) {
  return request({
    url: '/tron/fish/' + id +'/'+method,
    method: 'get'
  })
}

// 新增鱼苗管理
export function addFish(data) {
  return request({
    url: '/tron/fish',
    method: 'post',
    data: data
  })
}

// 修改鱼苗管理
export function updateFish(data) {
  return request({
    url: '/tron/fish',
    method: 'put',
    data: data
  })
}

// 置顶鱼苗管理
export function isTop(isTop,data) {
  return request({
    url: '/tron/fish/isTop/'+isTop,
    method: 'post',
    data: data
  })
}

// 删除鱼苗管理
export function delFish(id) {
  return request({
    url: '/tron/fish/' + id,
    method: 'delete'
  })
}

// 导出鱼苗管理
export function exportFish(query) {
  return request({
    url: '/tron/fish/export',
    method: 'get',
    params: query
  })
}
