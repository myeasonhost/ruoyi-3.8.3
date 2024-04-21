# 2.2 订单回调

:::tip
用户支付完成后，系统会自动向订单关联的回调地址（`notify_url`）发送通知消息，告知该笔订单已支付完成。
:::

## 回调地址
```shell:no-line-numbers
POST notify_url（商户在订单创建接口中指定）
```

## 通知参数
参数名 | 含义 | 类型 | 说明
:-|:-|:-|:-
pay_id | 官方订单号 | string(32) | MBUSDT 支付 ID。
order_id | 商讨自定义订单号 | string(64) | 商户自定义订单号，原样返回。
amount | 订单金额 | string(16) | 商户发起接口请求时指定的 `订单金额`。
currency | 订单货币单位 | string(8) | 商户发起接口请求时指定的 `订单币种`，固定为 `USDT`。
coin_code | 订单支付币种 | string(16) | 商户发起接口请求时指定的 `支付币种`
coin_amount | 订单支付金额 | string(16) | 订单显示的支付金额，由 `订单金额` + `订单币种` + `支付币种` 按照实时行情转换而来。
pay_time | 订单支付时间 | Long | 支付时间戳。
status | 订单状态 | string(16) | 代收订单状态，`1=支付中,2=支付成功，3=支付超时`
hash | 交易 hash | string(128) | 区块链交易 hash。商户可打开区块链浏览器查询交易详情。
sign | 签名串 | string(32) | 安全校验签名串。

:::tip
参数名 ASCII 码从小到大排序（字典序）<br>
`sign` 的生成规则为：`toLowerCase(md5(pay_id + order_id + amount + currency + coin_code + coin_amount + pay_time + hash + api key))`。
::: 

## 通知示例

```
amount=1&coin_amount=10&coin_code=USDT&currency=USD&hash=32421c1dd697913b31320be41da74a82b1bc45535daf7e08d24e0140b947f1ec
&order_id=1&pay_id=MB1626960689799114752&pay_time=1675352103000&status=2&sign=400152aa1a1d3002552585e1849daba9
```
    
## 通知返回

商户在收到通知信息后，可返回以下内容，告知已收到回调通知：

```
SUCCESS
```

## 通知重试

系统向商户创建订单时指定的 `notify_url` 发送 `回调通知` 后，如该 `notify_url` 回调返回的返回值不为 `SUCCESS`，则系统会触发 `重试机制`。相关规则如下：

1. 第一次通知：距离第一次10秒钟;。
2. 第二次通知：距离第二次1分钟。
3. 第三次通知: 距离第三次5分钟。
4. 三次通知不成功，请通知商户排查原因，则需要进行商户后台手动通知

:::tip
商户也可在后台 `支付订单` 页面，动手触发 `回调通知`。
:::
