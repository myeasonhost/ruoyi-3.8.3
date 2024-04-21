# 1.1 接口约定

## 编码
数据编码统一为 `utf-8`。

## 密钥
接口文档中提到的 `api key`，都是指商户秘钥 `key`，可在商户后台密钥信息栏查看，也可以找客户获取。

## 字段选填
对于接口参数标记为 `选填` 的字段，可传入具体的值或者空字符串，如下面的具体样例...&redirect_url=null&...

## sign
为保障接口安全，系统端会对接收到的所有数据，使用 `sign` 匹配校验，防止数据被非法篡改。

参数名 ASCII 码从小到大排序（字典序）；<br>
参数名区分大小写；<br>

对于 `sign` 的拼接规则，举个例子：<br>
生成规则 = md5(amount + currency + coin_code + order_id + product_name + customer_id + notify_url + redirect_url + locale + key)<br>
具体样例 = md5(amount=100&coin_code=USDT&currency=USD&customer_id=22&locale=en-US&mch_id=shanghu1606&notify_url=
http://23123&order_id=123&product_name=测试&redirect_url=null&key=bdcbb1fab783xxxxx99dec4d75f0c34)<br>

java端核心算法

```shell:no-line-numbers
//（1）sign 签名验证与校验
    Map<String, Object> treeMap = new TreeMap<>(BeanUtil.beanToMap(payEntity));
    treeMap.remove("sign");
    StringBuffer orgin = new StringBuffer();
    Iterator iter = treeMap.keySet().iterator();
    while (iter.hasNext()) {
        String name = (String) iter.next();
        orgin.append("&").append(name).append("=").append(treeMap.get(name));
    }
    orgin.append("&").append("key").append("=").append(orgAccountInfo.getPrivateKey());
    orgin.deleteCharAt(0);
    log.info("【支付订单】sign 加密串{}", orgin);
    String sign = DigestUtil.md5Hex(orgin.toString());
    log.info("【支付订单】sign 生成={}", sign);
    if (!sign.equals(payEntity.getSign())) {
        log.error("【支付订单】本地正确 sign={},错误三方 sign={}", sign, payEntity.getSign());
        return AjaxResult.error("sign 签名错误");
    }
```

## 数据提交
对于有数据提交的接口，统一使用 `POST` 的方式（查询接口除外）。相关请求头的 `content-type` 字段为：
```shell:no-line-numbers
application/x-www-form-urlencoded
```

## 交易查询

如接口有返回 `hash` 字段，商户可前往波场官方区块链浏览器（[https://tronscan.org](https://tronscan.org)），查询交易详细信息。
