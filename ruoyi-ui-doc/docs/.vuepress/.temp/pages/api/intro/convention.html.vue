<template><div><h1 id="_1-1-接口约定" tabindex="-1"><a class="header-anchor" href="#_1-1-接口约定"><span>1.1 接口约定</span></a></h1>
<h2 id="编码" tabindex="-1"><a class="header-anchor" href="#编码"><span>编码</span></a></h2>
<p>数据编码统一为 <code v-pre>utf-8</code>。</p>
<h2 id="密钥" tabindex="-1"><a class="header-anchor" href="#密钥"><span>密钥</span></a></h2>
<p>接口文档中提到的 <code v-pre>api key</code>，都是指商户秘钥 <code v-pre>key</code>，可在商户后台密钥信息栏查看，也可以找客户获取。</p>
<h2 id="字段选填" tabindex="-1"><a class="header-anchor" href="#字段选填"><span>字段选填</span></a></h2>
<p>对于接口参数标记为 <code v-pre>选填</code> 的字段，可传入具体的值或者空字符串，如下面的具体样例...&amp;redirect_url=null&amp;...</p>
<h2 id="sign" tabindex="-1"><a class="header-anchor" href="#sign"><span>sign</span></a></h2>
<p>为保障接口安全，系统端会对接收到的所有数据，使用 <code v-pre>sign</code> 匹配校验，防止数据被非法篡改。</p>
<p>参数名 ASCII 码从小到大排序（字典序）；<br>
参数名区分大小写；<br></p>
<p>对于 <code v-pre>sign</code> 的拼接规则，举个例子：<br>
生成规则 = md5(amount + currency + coin_code + order_id + product_name + customer_id + notify_url + redirect_url + locale + key)<br>
具体样例 = md5(amount=100&amp;coin_code=USDT&amp;currency=USD&amp;customer_id=22&amp;locale=en-US&amp;mch_id=shanghu1606&amp;notify_url=
http://23123&amp;order_id=123&amp;product_name=测试&amp;redirect_url=null&amp;key=bdcbb1fab783xxxxx99dec4d75f0c34)<br></p>
<p>java端核心算法</p>
<div class="language-bash" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code>//（1）sign 签名验证与校验
    Map<span class="token operator">&lt;</span>String, Object<span class="token operator">></span> treeMap <span class="token operator">=</span> new TreeMap<span class="token operator">&lt;></span><span class="token punctuation">(</span>BeanUtil.beanToMap<span class="token punctuation">(</span>payEntity<span class="token punctuation">))</span><span class="token punctuation">;</span>
    treeMap.remove<span class="token punctuation">(</span><span class="token string">"sign"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    StringBuffer orgin <span class="token operator">=</span> new StringBuffer<span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    Iterator iter <span class="token operator">=</span> treeMap.keySet<span class="token punctuation">(</span><span class="token punctuation">)</span>.iterator<span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token keyword">while</span> <span class="token punctuation">(</span>iter.hasNext<span class="token punctuation">(</span><span class="token punctuation">))</span> <span class="token punctuation">{</span>
        String name <span class="token operator">=</span> <span class="token punctuation">(</span>String<span class="token punctuation">)</span> iter.next<span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        orgin.append<span class="token punctuation">(</span><span class="token string">"&amp;"</span><span class="token punctuation">)</span>.append<span class="token punctuation">(</span>name<span class="token punctuation">)</span>.append<span class="token punctuation">(</span><span class="token string">"="</span><span class="token punctuation">)</span>.append<span class="token punctuation">(</span>treeMap.get<span class="token punctuation">(</span>name<span class="token punctuation">))</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    orgin.append<span class="token punctuation">(</span><span class="token string">"&amp;"</span><span class="token punctuation">)</span>.append<span class="token punctuation">(</span><span class="token string">"key"</span><span class="token punctuation">)</span>.append<span class="token punctuation">(</span><span class="token string">"="</span><span class="token punctuation">)</span>.append<span class="token punctuation">(</span>orgAccountInfo.getPrivateKey<span class="token punctuation">(</span><span class="token punctuation">))</span><span class="token punctuation">;</span>
    orgin.deleteCharAt<span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    log.info<span class="token punctuation">(</span><span class="token string">"【支付订单】sign 加密串{}"</span>, orgin<span class="token punctuation">)</span><span class="token punctuation">;</span>
    String sign <span class="token operator">=</span> DigestUtil.md5Hex<span class="token punctuation">(</span>orgin.toString<span class="token punctuation">(</span><span class="token punctuation">))</span><span class="token punctuation">;</span>
    log.info<span class="token punctuation">(</span><span class="token string">"【支付订单】sign 生成={}"</span>, sign<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span><span class="token operator">!</span>sign.equals<span class="token punctuation">(</span>payEntity.getSign<span class="token punctuation">(</span><span class="token punctuation">))</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        log.error<span class="token punctuation">(</span><span class="token string">"【支付订单】本地正确 sign={},错误三方 sign={}"</span>, sign, payEntity.getSign<span class="token punctuation">(</span><span class="token punctuation">))</span><span class="token punctuation">;</span>
        <span class="token builtin class-name">return</span> AjaxResult.error<span class="token punctuation">(</span><span class="token string">"sign 签名错误"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
</code></pre></div><h2 id="数据提交" tabindex="-1"><a class="header-anchor" href="#数据提交"><span>数据提交</span></a></h2>
<p>对于有数据提交的接口，统一使用 <code v-pre>POST</code> 的方式（查询接口除外）。相关请求头的 <code v-pre>content-type</code> 字段为：</p>
<div class="language-bash" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code>application/x-www-form-urlencoded
</code></pre></div><h2 id="交易查询" tabindex="-1"><a class="header-anchor" href="#交易查询"><span>交易查询</span></a></h2>
<p>如接口有返回 <code v-pre>hash</code> 字段，商户可前往波场官方区块链浏览器（<a href="https://tronscan.org" target="_blank" rel="noopener noreferrer">https://tronscan.org<ExternalLinkIcon/></a>），查询交易详细信息。</p>
</div></template>


