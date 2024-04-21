# 3.1 订单创建

:::tip
（1）提现出U自动回调下分，在提现成功后，系统将即时进行 `回调通知`。<br>
（2）需要商户在后台配置`业务中心->站内账户`其中一个付款地址需要是开启状态 <br>
:::

## 接口地址

```shell:no-line-numbers
POST http://54.179.118.170/api/pdai/create
```

## 接口参数

参数名 | 含义 | 验证 | 类型 | 说明
:-|:-|:-|:-|:-
mch_id | 商户账户	| 必填 | string(16)	| 商户对接账户名。
amount | 订单金额 | 必填 | string(16) | 精确到小数点后2位。
currency | 订单币种 | 必填 | string(8) | 订单币种单位。支持：`CNY`、`USD`。
coin_code | 支付币种 | 必填 | string(16) | 订单支付币种。固定为 `USDT`。
notify_url | 完成后回调通知地址 | 选填 | string(256) | 1. 用户支付完成后，系统会发送一个 `post` 消息到这个地址。<br/>2. 该参数不需要 `urlencode`。例如：http://www.xxx.com/pay_notify。<br/> 3. 商户在创建订单过程中传递参数进行配置。
order_id | 商户端订单号 | 必填 | string(64) | 系统在通知商户接口时，会带上这个参数。例：C15S6S3S1221。
customer_id | 商户端用户编号 | 必填 | string(64) | 可以为用户名，也可以为数据库中的用户编号。例：xxx@aaa.com，xxx等。
product_name | 商户端产品名称 | 必填 | string(64) | 用户收款的钱包地址。
address | 收款地址 | 必填 | string(64) | 该参数会显示在官方收银台页面顶部，留空则显示默认值。
sign | 签名串 | 必填 | string(32) | 安全校验签名串。

:::tip
参数名 ASCII 码从小到大排序（字典序）<br>
`sign` 的生成规则为：`toLowerCase(md5(mch_id + amount + currency + coin_code + order_id + product_name + customer_id + notify_url + address + api key))`。
:::

## 接口返回
参数名 | 含义 | 类型 | 说明
:-|:-|:-|:-
amount | 订单金额 | string(16) | 订单金额，原数据返回
currency | 订单币种 | string(8) | 订单币种，原数据返回
coin_code | 订单支付币种 | string(16) | 支付币种，原数据返回
coin_amount | 订单支付金额 | string(16) | 支付金额，由 `订单金额` + `订单币种` + `支付币种` 按照实时行情转换而来。
coin_address | 用户收款地址 | string(64) | 接收用户支付的地址，以字母 `T` 开头。
out_address | 商户转出地址 | string(64) | 商户支付的地址，以字母 `T` 开头。

请求示例：
```shell:no-line-numbers
http://54.179.118.170/api/pdai/create?order_id=1&mch_id=test01&productId=1&status=2&mchOrderNo=1&paySuccTime=13123213213
&sign=76c6c0997f8e5d9eb28b35920df69143&notify_url=http://23123&amount=1&customer_id=22&product_name=测试&address=3123123
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
        "amount": "1",
        "currency": "USD",
        "coin_code": "USDT",
        "coin_amount": "1",
        "coin_address": "TQoSZZByWYwmXXXXXXacASUV2b9ZaQw",
        "out_address": "TK2C3sfCzVmXXXXXXXepzoR1DB8ZAEgm"
    }
}
```
:::tip
code=200为成功返回，msg="操作成功"<br>
code=500为错误返回，msg="错误原因"<br>
:::

    
