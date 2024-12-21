/*
 * @Author: your name
 * @Date: 2020-05-01 12:05:05
 * @LastEditTime: 2020-05-09 20:26:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \moban_cli4\src\api\http.js
 */


/**
 * axios封装
 * 请求拦截、响应拦截、错误统一处理
 */
import axios from 'axios';
import router from '../router/index.js';
import store from '../store/index';
// import { Message } from "element-ui";

/**
 * 提示函数
 * 禁止点击蒙层、显示一秒后关闭
 */
// const tip = msg => {
//     Message({
//         message: msg,
//         duration: 1000,
//         forbidClick: true
//     });
// }
const tip=msg => {
  this.$Message.info(msg);
}
/**
 * 跳转登录页
 * 携带当前页面路由，以期在登录页面完成登录后返回当前页面
 */
const toLogin = () => {
    router.replace({
        path: '/login',
        query: {
            redirect: router.currentRoute.fullPath
        }
    });
}

/**
 * 请求失败后的错误统一处理
 * @param {Number} status 请求失败的状态码
 */
const errorHandle = (status, other) => {
    // 状态码判断
    switch (status) {
        // 401: 未登录状态，跳转登录页
        case 401:
            toLogin();
            break;
        // 403 token过期
        // 清除token并跳转登录页
        case 403:
            tip('登录过期，请重新登录');
            localStorage.removeItem('token');
            store.commit('loginSuccess', null);
            setTimeout(() => {
                toLogin();
            }, 1000);
            break;
        // 404请求不存在
        case 404:
            tip('请求的资源不存在');
            break;
        default:
            console.log(other);
        }}

// 创建axios实例
var $axios = axios.create({    timeout: 1000 * 12});
// 设置post请求头
$axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
/**
 * 请求拦截器
 * 每次请求前，如果存在token则在请求头中携带token
 */
$axios.interceptors.request.use(
    config => {
        // 登录流程控制中，根据本地是否存在token判断用户的登录情
        // 但是即使token存在，也有可能token是过期的，所以在每次的请求头中携带token
        // 后台根据携带的token判断用户的登录情况，并返回给我们对应的状态码
        // 而后我们可以在响应拦截器中，根据状态码进行一些统一的操作。
        // config.headers = {
        //     'content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        //     }
        config.data=JSON.stringify(config.data)
        const token = store.state.token;
        token && (config.headers.Authorization = token);
        return config;
    },
    error => Promise.error(error))

// 响应拦截器
$axios.interceptors.response.use(
    // 请求成功
    res => res.status === 200 ? Promise.resolve(res) : Promise.reject(res),
    // 请求失败
    error => {
        const { response } = error;
        if (response) {
            // 请求已发出，但是不在2xx的范围
            errorHandle(response.status, response.data.message);
            return Promise.reject(response);
        } else {
            // 处理断网的情况
            // eg:请求超时或断网时，更新state的network状态
            // network状态在app.vue中控制着一个全局的断网提示组件的显示隐藏
            // 关于断网组件中的刷新重新获取数据，会在断网组件中说明

            console.log(33)
            // store.commit('changeNetwork', false);
        }
    });

export default $axios;
// /**axios封装
//  * 请求拦截、相应拦截、错误统一处理
//  */
// import axios from 'axios';
// import router from '../router/index.js'
// import qs from 'qs';
// // import { Toast } from 'vant';
// import { Message } from "element-ui";
// import store from '../store/index'
// const $axios = axios.create({    timeout: 1000 * 12});
// // 环境的切换
// if (process.env.NODE_ENV == 'development') {
//     axios.defaults.baseURL = '/api';
// } else if (process.env.NODE_ENV == 'debug') {
//     axios.defaults.baseURL = '';
// } else if (process.env.NODE_ENV == 'production') {
//     axios.defaults.baseURL = 'http://api.123dailu.com/';
// }

// // 请求超时时间
// // const $axios = axios.create({    timeout: 1000 * 12});
// $axios.defaults.timeout = 10000;

// // post请求头
// $axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

// // 请求拦截器
// $axios.interceptors.request.use(
//     config => {
//         // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token，不用每次请求都手动添加了
//         // 即使本地存在token，也有可能token是过期的，所以在响应拦截器中要对返回状态进行判断
//         const token = store.state.token;
//         token && (config.headers.Authorization = token);
//         return config;
//     },
//     error => {
//         return Promise.error(error);
//     })

// // 响应拦截器
// $axios.interceptors.response.use(
//     response => {
//         if (response.status === 200) {
//             return Promise.resolve(response);
//         } else {
//             return Promise.reject(response);
//         }
//     },
//     // 服务器状态码不是200的情况
//     error => {
//         if (error.response.status) {
//             switch (error.response.status) {
//                 // 401: 未登录
//                 // 未登录则跳转登录页面，并携带当前页面的路径
//                 // 在登录成功后返回当前页面，这一步需要在登录页操作。
//                 case 401:
//                     // /
//                     break;
//                 // 403 token过期
//                 // 登录过期对用户进行提示
//                 // 清除本地token和清空vuex中token对象
//                 // 跳转登录页面
//                 case 403:
//                 Message({
//                         message: '登录过期，请重新登录',
//                         duration: 1000,
//                         forbidClick: true
//                     });
//                     // 清除token
//                     localStorage.removeItem('token');
//                     store.commit('loginSuccess', null);
//                     // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
//                     setTimeout(() => {
//                         // router.replace({
//                         //     path: '/login',
//                         //     query: {
//                         //         redirect: router.currentRoute.fullPath
//                         //     }
//                         // });
//                     }, 1000);
//                     break;
//                 // 404请求不存在
//                 case 404:
//                     Message({
//                         message: '网络请求不存在',
//                         duration: 1500,
//                         forbidClick: true
//                     });
//                 break;
//                 // 其他错误，直接抛出错误提示
//                 default:
//                     Message({
//                         message: error.response.data.message,
//                         duration: 1500,
//                         forbidClick: true
//                     });
//             }
//             return Promise.reject(error.response);
//         }
//     }
// );
// /**
//  * get方法，对应get请求
//  * @param {String} url [请求的url地址]
//  * @param {Object} params [请求时携带的参数]
//  */
// // export function get(url, params){
// //     return new Promise((resolve, reject) =>{
// //         axios.get(url, {
// //             params: params
// //         })
// //         .then(res => {
// //             resolve(res.data);
// //         })
// //         .catch(err => {
// //             reject(err.data)
// //         })
// //     });
// // }
// // /**
// //  * post方法，对应post请求
// //  * @param {String} url [请求的url地址]
// //  * @param {Object} params [请求时携带的参数]
// //  */
// // export function post(url, params) {
// //     return new Promise((resolve, reject) => {
// //         axios.post(url, QS.stringify(params))
// //         .then(res => {
// //             resolve(res.data);
// //         })
// //         .catch(err => {
// //             reject(err.data)
// //         })
// //     });
// // }
// export default $axios;