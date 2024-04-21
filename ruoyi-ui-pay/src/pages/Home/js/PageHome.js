import QRCode from 'qrcodejs2'
import parseData from './parseData.js'
import lang from './lang.js'

export default {
  data() {
    return {
      lang,
      language: 'en-US',
      url: '',
      creatTime: '',
      amount: '',
      expireDate: '',
      orderId: '',
      mchId: '',
      currency: '',
      orderInfo: {},
      time: {},
      timer: null
    }
  },
  computed: {
    langConfig() {
      return lang[this.language] || {}
    }
  },
  methods: {
    $t(text) {
      return this.langConfig[text] || text
    },
    creatQrCode() {
      const qrcode = new QRCode(this.$refs.qrCodeUrl, {
        text: this.url,
        width: 120,
        height: 120,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
      })
      console.log(qrcode)
    },
    downloadQrCode() {
      const canvasData = this.$refs.qrCodeUrl.getElementsByTagName('canvas')
      const a = document.createElement('a')
      a.href = canvasData[0].toDataURL('image/png')
      a.download = 'drcQrcode'
      a.click()
    },
    copyAddress(address) {
      // 模拟 输入框
      const cInput = document.createElement('input')
      cInput.value = address
      document.body.appendChild(cInput)
      // 选取文本框内容
      cInput.select()
      document.execCommand('copy')
      // 复制成功后再将构造的标签 移除
      document.body.removeChild(cInput)
      this.shareLiveAddress = ''
      this.shareDialogVisable = true
      this.$message.success('Copy successfully')
    },
    setDownTime() {
      this.timer = setInterval(() => {
        const expireDateTime = this.creatTime + (this.expireDate * 60000)
        let time = expireDateTime - Number(new Date())
        if (time <= 0) {
          clearInterval(this.timer)
          time = 0
        }
        const obj = {}
        obj.hh = (parseInt(time / 3600000)) < 10 ? '0' + parseInt(time / 3600000) : parseInt(time / 3600000)
        obj.mm = (parseInt(time / 60000) % 60) < 10 ? '0' + (parseInt(time / 60000) % 60) : (parseInt(time / 60000) % 60)
        obj.ss = (parseInt(time / 1000) % 60) < 10 ? '0' + parseInt(time / 1000) % 60 : parseInt(time / 1000) % 60
        this.time = obj
      }, 1000)
    },
    async getOrderStatus() {
      const res = await this.$http.get({
        url: 'api/pay/order/queryStatus',
        data: {
          mch_id: this.mchId,
          order_id: this.orderId
        }
      })
      this.orderInfo.status = 1
      if (res && res.code === 200) {
        this.orderInfo = res.data
      }
    }
  },
  mounted() {
    try {
      console.log(this.$route.fullPath)
      const fullParams = this.$route.fullPath.split('?')[1]
      const params = {}
      const urlParams = fullParams.split('&')
      for (const iterator of urlParams) {
        const keyVal = iterator.split('=')
        params[keyVal[0]] = keyVal[1]
      }
      console.log(params, 'params', urlParams)
      const arr = parseData(params.data, params.key).split(',')
      this.url = arr[0]
      this.amount = Number(arr[1])
      this.expireDate = Number(arr[2])
      this.creatTime = Number(arr[3])
      this.orderId = arr[4]
      this.mchId = arr[5]
      this.currency = arr[6]
      this.language = params.locale
      this.creatQrCode()
      this.setDownTime()
      this.getOrderStatus()
      setInterval(() => {
        this.getOrderStatus()
      }, 5000)
    } catch (e) {
      console.log(e)
    }
  }
}
