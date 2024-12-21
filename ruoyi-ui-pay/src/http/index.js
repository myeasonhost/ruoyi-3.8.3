import axios from 'axios'

const source = axios.CancelToken.source()
export default class Http {
  static axios ({ method, url, data }) {
    const headers = {
      'Content-Type': 'application/json'
    }
    console.error('接口' + method + '： /api' + url + ' 参数：', JSON.stringify(data))
    return new Promise((resolve, reject) => {
      const config = {
        method: method,
        url: '/' + url,
        timeout: 1000 * 20,
        headers: headers,
        emulateJSON: true,
        cancelToken: source.token
      }
      if (method.toUpperCase() === 'POST') {
        config.data = data
      } else if (method.toUpperCase() === 'GET') {
        config.params = data
      }
      axios(config).then(res => {
        console.info('接口： /api' + url + ' 结果：', res)
        resolve(this.doError(res))
      }).catch(err => {
        console.error(err)
        resolve(this.doError(null))
      })
    })
  }

  static get ({ url, data }) {
    return new Promise((resolve, reject) => {
      this.axios({
        method: 'GET',
        url,
        data
      }).then(res => {
        resolve(res)
      }).catch(err => {
        console.error(err)
        resolve(null)
      })
    })
  }

  static post ({ url, data }) {
    return new Promise((resolve, reject) => {
      this.axios({
        method: 'POST',
        url,
        data
      }).then(res => {
        resolve(res)
      }).catch(err => {
        console.error(err)
        resolve(null)
      })
    })
  }

  static all (requests) {
    return new Promise((resolve, reject) => {
      const arr = []
      for (const item of requests) {
        arr.push(this.axios({ method: item.method.toUpperCase(), url: item.url, data: item.data }))
      }
      axios.all(arr).then(axios.spread(function () {
        resolve(arguments)
      })).catch(err => {
        console.error(err)
        resolve(null)
      })
    })
  }

  static doError (res) {
    if (res && res.status === 200 && res.data) {
      switch (res.data.code) {
        case 200:
          return res.data
        default:
      }
    } else {
      this.messageError('The network status is unstable')
    }
  }

}
