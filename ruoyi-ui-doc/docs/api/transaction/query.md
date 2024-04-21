# 2.3 订单查询

:::tip
该接口供商户主动查询订单状态。多用于商户自定义收银台的场景。
:::

## 接口地址

```shell:no-line-numbers
GET/POST http://54.179.118.170/api/pay/order/queryStatus
```

## 接口参数
参数名 | 含义 | 验证 | 类型 | 说明
:-|:-|:-|:-|:-
mch_id | 商户账号 | 必填 | string(16) | 商户对接账号名。
order_id | 商户端订单号 | 必填 | string(64) | 系统在通知商户接口时，会带上这个参数。例：C15S6S3S1221。
sign | 签名串 | 必填 | string(32) | 安全校验签名串。


:::tip
`URL` 请求示例：`http://54.179.118.170/api/pay/order/queryStatus?mch_id=test01&order_id=C15S6S3S1221&sign=baa4370b721b12fd2a44ecfffd0c09a4`
:::

## 接口返回

```json:no-line-numbers  
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "mchId": "test01",
        "orderId": "C15S6S3S1221",
        "status": "3",
        "notify_succeed": null,
        "amount": "8.5",
        "coin_amount": "8.80"
    }
}
```

:::tip
当 `data.status` 字段值为 `1=支付中,2=支付成功，3=支付超时` 时，代表订单已支付，否则代表订单未支付。<br>
当 `notify_succeed` 字段值为 `0=未通知（null），1=通知成功，2=通知失败 ` 时，代表回调已成功，否则代表回调未成功。<br>
code=200为成功返回，msg="操作成功"<br>
code=500为错误返回，msg="错误原因"<br>
:::
