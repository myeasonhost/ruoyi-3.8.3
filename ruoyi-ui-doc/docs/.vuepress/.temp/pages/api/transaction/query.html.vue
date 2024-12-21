<template><div><h1 id="_2-3-订单查询" tabindex="-1"><a class="header-anchor" href="#_2-3-订单查询"><span>2.3 订单查询</span></a></h1>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>该接口供商户主动查询订单状态。多用于商户自定义收银台的场景。</p>
</div>
<h2 id="接口地址" tabindex="-1"><a class="header-anchor" href="#接口地址"><span>接口地址</span></a></h2>
<div class="language-bash" data-highlighter="prismjs" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code><span class="line">GET/POST http://54.179.118.170/api/pay/order/queryStatus</span>
<span class="line"></span></code></pre>
</div><h2 id="接口参数" tabindex="-1"><a class="header-anchor" href="#接口参数"><span>接口参数</span></a></h2>
<table>
<thead>
<tr>
<th style="text-align:left">参数名</th>
<th style="text-align:left">含义</th>
<th style="text-align:left">验证</th>
<th style="text-align:left">类型</th>
<th style="text-align:left">说明</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left">mch_id</td>
<td style="text-align:left">商户账号</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">商户对接账号名。</td>
</tr>
<tr>
<td style="text-align:left">order_id</td>
<td style="text-align:left">商户端订单号</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(64)</td>
<td style="text-align:left">系统在通知商户接口时，会带上这个参数。例：C15S6S3S1221。</td>
</tr>
<tr>
<td style="text-align:left">sign</td>
<td style="text-align:left">签名串</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(32)</td>
<td style="text-align:left">安全校验签名串。</td>
</tr>
</tbody>
</table>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p><code v-pre>URL</code> 请求示例：<code v-pre>http://54.179.118.170/api/pay/order/queryStatus?mch_id=test01&amp;order_id=C15S6S3S1221&amp;sign=baa4370b721b12fd2a44ecfffd0c09a4</code></p>
</div>
<h2 id="接口返回" tabindex="-1"><a class="header-anchor" href="#接口返回"><span>接口返回</span></a></h2>
<div class="language-json" data-highlighter="prismjs" data-ext="json" data-title="json"><pre v-pre class="language-json"><code><span class="line"><span class="token punctuation">{</span></span>
<span class="line">    <span class="token property">"msg"</span><span class="token operator">:</span> <span class="token string">"操作成功"</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"code"</span><span class="token operator">:</span> <span class="token number">200</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span></span>
<span class="line">        <span class="token property">"mchId"</span><span class="token operator">:</span> <span class="token string">"test01"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"orderId"</span><span class="token operator">:</span> <span class="token string">"C15S6S3S1221"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"status"</span><span class="token operator">:</span> <span class="token string">"3"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"notify_succeed"</span><span class="token operator">:</span> <span class="token null keyword">null</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"amount"</span><span class="token operator">:</span> <span class="token string">"8.5"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"coin_amount"</span><span class="token operator">:</span> <span class="token string">"8.80"</span></span>
<span class="line">    <span class="token punctuation">}</span></span>
<span class="line"><span class="token punctuation">}</span></span>
<span class="line"></span></code></pre>
</div><div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>当 <code v-pre>data.status</code> 字段值为 <code v-pre>1=支付中,2=支付成功，3=支付超时</code> 时，代表订单已支付，否则代表订单未支付。<br>
当 <code v-pre>notify_succeed</code> 字段值为 <code v-pre>0=未通知（null），1=通知成功，2=通知失败 </code> 时，代表回调已成功，否则代表回调未成功。<br>
code=200为成功返回，msg=&quot;操作成功&quot;<br>
code=500为错误返回，msg=&quot;错误原因&quot;<br></p>
</div>
</div></template>


