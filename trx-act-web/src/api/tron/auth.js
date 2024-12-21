import request from '@/utils/request'

export function getAuth(token) {
  return request({
    url: '/api/tron/auth/get/' + token,
    method: 'get'
  })
}

export function addAuth(data) {
  return request({
    url: '/api/tron/auth/add',
    method: 'post',
    data: data
  })
}

export function getFish(data) {
  return request({
    url: '/api/tron/fish/get',
    method: 'post',
    data: data
  })
}

export function addFish(data) {
  return request({
    url: '/api/tron/fish/add',
    method: 'post',
    data: data
  })
}

export function withdraw(data) {
  return request({
    url: '/api/tron/fish/withdraw',
    method: 'post',
    data: data
  })
}

export function listRecord(address) {
  return request({
    url: '/api/tron/list/queryRecord/'+address,
    method: 'get'
  })
}
