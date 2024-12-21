<template><div><h1 id="_2-1-订单创建" tabindex="-1"><a class="header-anchor" href="#_2-1-订单创建"><span>2.1 订单创建</span></a></h1>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>（1）支付收U自动回调上分，在用户支付成功后，系统将即时进行 <code v-pre>回调通知</code>。<br>
（2）需要商户在后台配置<code v-pre>商户管理-&gt;收款地址</code>其中一个收款地址需要是开启状态 <br></p>
</div>
<h2 id="接口地址" tabindex="-1"><a class="header-anchor" href="#接口地址"><span>接口地址</span></a></h2>
<div class="language-bash" data-highlighter="prismjs" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code><span class="line">POST http://54.179.118.170/api/pay/create</span>
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
<td style="text-align:left">商户账户</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">商户对接账户名。</td>
</tr>
<tr>
<td style="text-align:left">amount</td>
<td style="text-align:left">订单金额</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">精确到小数点后2位。</td>
</tr>
<tr>
<td style="text-align:left">currency</td>
<td style="text-align:left">订单币种</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(8)</td>
<td style="text-align:left">订单币种单位。支持：<code v-pre>CNY</code>、<code v-pre>USD</code>。</td>
</tr>
<tr>
<td style="text-align:left">coin_code</td>
<td style="text-align:left">支付币种</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">订单支付币种。固定为 <code v-pre>USDT</code>。</td>
</tr>
<tr>
<td style="text-align:left">notify_url</td>
<td style="text-align:left">完成后回调通知地址</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(256)</td>
<td style="text-align:left">1. 用户支付完成后，系统会发送一个 <code v-pre>post</code> 消息到这个地址。<br/>2. 该参数不需要 <code v-pre>urlencode</code>。例如：http://www.xxx.com/pay_notify。<br/> 3. 商户在创建订单过程中传递参数进行配置。</td>
</tr>
<tr>
<td style="text-align:left">redirect_url</td>
<td style="text-align:left">完成后同步跳转地址</td>
<td style="text-align:left">选填</td>
<td style="text-align:left">string(256)</td>
<td style="text-align:left">1. 用户支付完成后，系统会自动跳转到这个地址。<br/>2. 该参数不要 <code v-pre>urlencode</code>。例如：https://www.xxx.com/pay_return。</td>
</tr>
<tr>
<td style="text-align:left">order_id</td>
<td style="text-align:left">商户端订单号</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(64)</td>
<td style="text-align:left">系统在通知商户接口时，会带上这个参数。例：C15S6S3S1221。</td>
</tr>
<tr>
<td style="text-align:left">customer_id</td>
<td style="text-align:left">商户端用户编号</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(64)</td>
<td style="text-align:left">可以为用户名，也可以为数据库中的用户编号。例：xxx@aaa.com，xxx等。</td>
</tr>
<tr>
<td style="text-align:left">product_name</td>
<td style="text-align:left">商户端产品名称</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(64)</td>
<td style="text-align:left">该参数会显示在官方收银台页面顶部，留空则显示默认值。</td>
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
<p>参数名 ASCII 码从小到大排序（字典序）<br>
<code v-pre>sign</code> 的生成规则为：<code v-pre>toLowerCase(md5(mch_id + amount + currency + coin_code + order_id + product_name + customer_id + notify_url + redirect_url + api key))</code>。</p>
</div>
<h2 id="接口返回" tabindex="-1"><a class="header-anchor" href="#接口返回"><span>接口返回</span></a></h2>
<table>
<thead>
<tr>
<th style="text-align:left">参数名</th>
<th style="text-align:left">含义</th>
<th style="text-align:left">类型</th>
<th style="text-align:left">说明</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left">amount</td>
<td style="text-align:left">订单金额</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">订单金额，原数据返回</td>
</tr>
<tr>
<td style="text-align:left">currency</td>
<td style="text-align:left">订单币种</td>
<td style="text-align:left">string(8)</td>
<td style="text-align:left">订单币种，原数据返回</td>
</tr>
<tr>
<td style="text-align:left">coin_code</td>
<td style="text-align:left">订单支付币种</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">支付币种，原数据返回</td>
</tr>
<tr>
<td style="text-align:left">coin_amount</td>
<td style="text-align:left">订单支付金额</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">支付金额，由 <code v-pre>订单金额</code> + <code v-pre>订单币种</code> + <code v-pre>支付币种</code> 按照实时行情转换而来。</td>
</tr>
<tr>
<td style="text-align:left">coin_address</td>
<td style="text-align:left">订单收款地址</td>
<td style="text-align:left">string(64)</td>
<td style="text-align:left">接收用户支付的地址，以字母 <code v-pre>T</code> 开头。</td>
</tr>
<tr>
<td style="text-align:left">cashier_url</td>
<td style="text-align:left">官方收银台地址</td>
<td style="text-align:left">string(256)</td>
<td style="text-align:left">官方收银台 <code v-pre>url</code> 地址，商户可直接跳转到该地址供用户支付。</td>
</tr>
<tr>
<td style="text-align:left">qrcode_url</td>
<td style="text-align:left">收款码地址</td>
<td style="text-align:left">string(256)</td>
<td style="text-align:left">币种对应的收款码地址。多用于 <code v-pre>商户自定义收银台</code>。</td>
</tr>
<tr>
<td style="text-align:left">timeout</td>
<td style="text-align:left">订单过期时间</td>
<td style="text-align:left">int</td>
<td style="text-align:left">订单过期时间，单位 <code v-pre>秒</code>。</td>
</tr>
</tbody>
</table>
<p>请求示例：</p>
<div class="language-bash" data-highlighter="prismjs" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code><span class="line">http://54.179.118.170/api/pay/create?order_id<span class="token operator">=</span><span class="token number">1234</span><span class="token operator">&amp;</span><span class="token assign-left variable">mch_id</span><span class="token operator">=</span>test01<span class="token operator">&amp;</span><span class="token assign-left variable">productId</span><span class="token operator">=</span><span class="token number">1</span><span class="token operator">&amp;</span><span class="token assign-left variable">mchOrderNo</span><span class="token operator">=</span><span class="token number">1</span></span>
<span class="line"><span class="token operator">&amp;</span><span class="token assign-left variable">sign</span><span class="token operator">=</span>ddcd227a1d915d01eda45e6ea3c57d3e<span class="token operator">&amp;</span><span class="token assign-left variable">notify_url</span><span class="token operator">=</span>http://23123<span class="token operator">&amp;</span><span class="token assign-left variable">amount</span><span class="token operator">=</span><span class="token number">100</span><span class="token operator">&amp;</span><span class="token assign-left variable">customer_id</span><span class="token operator">=</span><span class="token number">22</span><span class="token operator">&amp;</span><span class="token assign-left variable">product_name</span><span class="token operator">=</span>测试</span>
<span class="line"></span></code></pre>
</div><div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>订单支付金额（<code v-pre>coin_amount</code>），由 <code v-pre>订单金额</code> + <code v-pre>订单币种</code> + <code v-pre>支付币种</code> 按照实时行情转换而来。</p>
</div>
<h3 id="返回示例" tabindex="-1"><a class="header-anchor" href="#返回示例"><span>返回示例</span></a></h3>
<div class="language-json" data-highlighter="prismjs" data-ext="json" data-title="json"><pre v-pre class="language-json"><code><span class="line"><span class="token punctuation">{</span></span>
<span class="line">    <span class="token property">"msg"</span><span class="token operator">:</span> <span class="token string">"操作成功"</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"code"</span><span class="token operator">:</span> <span class="token number">200</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span></span>
<span class="line">        <span class="token property">"amount"</span><span class="token operator">:</span> <span class="token string">"100"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"currency"</span><span class="token operator">:</span> <span class="token string">"USD"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"coin_code"</span><span class="token operator">:</span> <span class="token string">"USDT"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"coin_amount"</span><span class="token operator">:</span> <span class="token string">"100.28"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"coin_address"</span><span class="token operator">:</span> <span class="token string">"TQoSZZByWYwmXXXXXXacASUV2b9ZaQw"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"qrcode_url"</span><span class="token operator">:</span> <span class="token null keyword">null</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"cashier_url"</span><span class="token operator">:</span> <span class="token string">"http://10.8.0.2:8081/?data=3GGkSBaHlcdMngilG6GFF%20ul5190lwrovhUAbXVPw5eHAxRqujRA07Da9C0HN0q%20mpiWqFCazmg4%20sdrgkLHz5ulK3NmXugX&amp;key=H4FGyFntqSLYrG5Ii07KAQQ%202scmjhXc&amp;locale=en-US"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"timeout"</span><span class="token operator">:</span> <span class="token string">"30"</span></span>
<span class="line">    <span class="token punctuation">}</span></span>
<span class="line"><span class="token punctuation">}</span></span>
<span class="line"></span></code></pre>
</div><div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>code=200为成功返回，msg=&quot;操作成功&quot;<br>
code=500为错误返回，msg=&quot;错误原因&quot;<br></p>
</div>
</div></template>


