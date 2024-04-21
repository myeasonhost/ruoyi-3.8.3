# 2.4 订单收银台

商户调用 `订单创建`（/api/pay/create） 接口后，会返回如下格式数据：
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
    
商户可直接从上述返回数据中，取出 `cashier_url` 字段，然后跳转至该地址供用户支付。

## 收银台
跳转至 `cashier_url` 后，页面显示如下(中英文收银台)：

<img src="/images/image01.png" alt="" width="360"/>
<img src="/images/image02.png" alt="" width="360"/>


## 跳转

用户在收银台支付成功后，系统会自动通知商户在创建订单时指定的回调地址（`notify_url`）。在间隔大概 `1` 秒钟后，页面会自动跳转至商户在创建订单时指定的跳转地址（`redirect_url`）。

:::tip
若商户在创建订单时未指定 `notify_url`，则系统会使用商户在后台统一配置的 `订单回调地址`。
:::

### 跳转地址
```shell:no-line-numbers
GET redirect_url（商户在订单创建接口中指定）
```

### 跳转参数

参数名 | 含义 | 类型 | 说明
:-|:-|:-|:-
order_id | 商户自定义订单号 |	string(32) | 商户可以通过 `order_id` 参数，查询本地数据库，确认用户是否支付成功，并给出相应的页面展示。

::: warning
请不要将此跳转作为用户支付成功的判断条件，此行为极不安全。请根据支付成功的回调通知是否送达，来判断用户是否支付成功。
:::
