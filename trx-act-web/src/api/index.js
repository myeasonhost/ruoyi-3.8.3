import $axios from './http.js'
// import qs from 'qs'
// $axios.interceptors.request.use((req) => {
//   if (req.method === 'post') {
//     req.data = qs.stringify(req.data);
//   }
//   return req;
// }, (error) => Promise.reject(error));

const base = 'http://47.75.90.65:8082/forward/api/router';
// const base = 'http://api.k780.com/?app=weather.today&weaid=1&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json';
// export const getUserList = params => { return $axios.get(`${base}/user`, { params: params }); };
// export const getUserList = params => { return $axios.post(`${base}/user/list`, qs.stringify(params)); };

// const aaa={
//     type: "index",
//     method: "pt.index.getIndexSwiper",
//     format: "json",
//     ver: "1.0.0",
//     appKey: "1000",
//     appSecret: "asdfghjkqwertyu",
//     sign: "39207ea77d74ab5a29ee22cc88342c5d",
//   }
export const ceshi = () => { return $axios.post(`${base}`, `type=index&method=pt.index.getIndexSwiper&format=json&ver=1.0.0&appKey=1000&appSecret=asdfghjkqwertyu&sign=39207ea77d74ab5a29ee22cc88342c5d`)};

export const getUserList = params => { return $axios.get(`${base}/user/list`, { params: params }); };

export const getUserListPage = params => { return $axios.get(`${base}/user/listpage`, { params: params }); };

export const removeUser = params => { return $axios.get(`${base}/user/remove`, { params: params }); };

export const batchRemoveUser = params => { return $axios.get(`${base}/user/batchremove`, { params: params }); };

export const editUser = params => { return $axios.post(`${base}/user/edit`, { params: params }); };

export const addUser = params => { return $axios.get(`${base}/user/add`, { params: params }); };
