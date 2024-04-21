# 2.1 订单创建

:::tip
（1）支付收U自动回调上分，在用户支付成功后，系统将即时进行 `回调通知`。<br>
（2）需要商户在后台配置`商户管理->收款地址`其中一个收款地址需要是开启状态 <br>
:::

## 接口地址

```shell:no-line-numbers
POST http://54.179.118.170/api/pay/create
```

## 接口参数

参数名 | 含义 | 验证 | 类型 | 说明
:-|:-|:-|:-|:-
mch_id | 商户账户	| 必填 | string(16)	| 商户对接账户名。
amount | 订单金额 | 必填 | string(16) | 精确到小数点后2位。
currency | 订单币种 | 必填 | string(8) | 订单币种单位。支持：`CNY`、`USD`。
coin_code | 支付币种 | 必填 | string(16) | 订单支付币种。固定为 `USDT`。
notify_url | 完成后回调通知地址 | 必填 | string(256) | 1. 用户支付完成后，系统会发送一个 `post` 消息到这个地址。<br/>2. 该参数不需要 `urlencode`。例如：http://www.xxx.com/pay_notify。<br/> 3. 商户在创建订单过程中传递参数进行配置。
redirect_url | 完成后同步跳转地址 | 选填 | string(256) | 1. 用户支付完成后，系统会自动跳转到这个地址。<br/>2. 该参数不要 `urlencode`。例如：https://www.xxx.com/pay_return。
order_id | 商户端订单号 | 必填 | string(64) | 系统在通知商户接口时，会带上这个参数。例：C15S6S3S1221。
customer_id | 商户端用户编号 | 必填 | string(64) | 可以为用户名，也可以为数据库中的用户编号。例：xxx@aaa.com，xxx等。
product_name | 商户端产品名称 | 必填 | string(64) | 该参数会显示在官方收银台页面顶部，留空则显示默认值。
sign | 签名串 | 必填 | string(32) | 安全校验签名串。

:::tip
参数名 ASCII 码从小到大排序（字典序）<br>
`sign` 的生成规则为：`toLowerCase(md5(mch_id + amount + currency + coin_code + order_id + product_name + customer_id + notify_url + redirect_url + api key))`。
:::

## 接口返回
参数名 | 含义 | 类型 | 说明
:-|:-|:-|:-
amount | 订单金额 | string(16) | 订单金额，原数据返回
currency | 订单币种 | string(8) | 订单币种，原数据返回
coin_code | 订单支付币种 | string(16) | 支付币种，原数据返回
coin_amount | 订单支付金额 | string(16) | 支付金额，由 `订单金额` + `订单币种` + `支付币种` 按照实时行情转换而来。
coin_address | 订单收款地址 | string(64) | 接收用户支付的地址，以字母 `T` 开头。
cashier_url | 官方收银台地址 | string(256) | 官方收银台 `url` 地址，商户可直接跳转到该地址供用户支付。
qrcode_url | 收款码地址 | string(256) | 币种对应的收款码地址。多用于 `商户自定义收银台`。
timeout | 订单过期时间 | int | 订单过期时间，单位 `秒`。

请求示例：
```shell:no-line-numbers
http://54.179.118.170/api/pay/create?order_id=1234&mch_id=test01&productId=1&mchOrderNo=1
&sign=ddcd227a1d915d01eda45e6ea3c57d3e&notify_url=http://23123&amount=100&customer_id=22&product_name=测试
```

:::tip
订单支付金额（`coin_amount`），由 `订单金额` + `订单币种` + `支付币种` 按照实时行情转换而来。
:::

### 返回示例
```json:no-line-numbers
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "amount": "100",
        "currency": "USD",
        "coin_code": "USDT",
        "coin_amount": "100.28",
        "coin_address": "TQoSZZByWYwmXXXXXXacASUV2b9ZaQw",
        "qrcode_url": null,
        "cashier_url": "http://52.52.52.209:85/?data=O96FK+SoHqqx50by98VkjOVTpCePTvtiZNlG7S6NJJTk7TZiSRQQzA==&key=/+61PS8TLkRPv+8ZcI4IW0PJEHbXRGl+",
        "timeout": "30"
    }
}
```
:::tip
code=200为成功返回，msg="操作成功"<br>
code=500为错误返回，msg="错误原因"<br>
:::

    
