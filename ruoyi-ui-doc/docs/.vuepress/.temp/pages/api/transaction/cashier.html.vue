<template><div><h1 id="_2-4-订单收银台" tabindex="-1"><a class="header-anchor" href="#_2-4-订单收银台"><span>2.4 订单收银台</span></a></h1>
<p>商户调用 <code v-pre>订单创建</code>（/api/pay/create） 接口后，会返回如下格式数据：</p>
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
</div><p>商户可直接从上述返回数据中，取出 <code v-pre>cashier_url</code> 字段，然后跳转至该地址供用户支付。</p>
<h2 id="收银台" tabindex="-1"><a class="header-anchor" href="#收银台"><span>收银台</span></a></h2>
<p>跳转至 <code v-pre>cashier_url</code> 后，页面显示如下(中英文收银台)：</p>
<img src="/images/image01.png" alt="" width="360"/>
<img src="/images/image02.png" alt="" width="360"/>
<h2 id="跳转" tabindex="-1"><a class="header-anchor" href="#跳转"><span>跳转</span></a></h2>
<p>用户在收银台支付成功后，系统会自动通知商户在创建订单时指定的回调地址（<code v-pre>notify_url</code>）。在间隔大概 <code v-pre>1</code> 秒钟后，页面会自动跳转至商户在创建订单时指定的跳转地址（<code v-pre>redirect_url</code>）。</p>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>若商户在创建订单时未指定 <code v-pre>notify_url</code>，则系统会使用商户在后台统一配置的 <code v-pre>订单回调地址</code>。</p>
</div>
<h3 id="跳转地址" tabindex="-1"><a class="header-anchor" href="#跳转地址"><span>跳转地址</span></a></h3>
<div class="language-bash" data-highlighter="prismjs" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code><span class="line">GET redirect_url（商户在订单创建接口中指定）</span>
<span class="line"></span></code></pre>
</div><h3 id="跳转参数" tabindex="-1"><a class="header-anchor" href="#跳转参数"><span>跳转参数</span></a></h3>
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
<td style="text-align:left">order_id</td>
<td style="text-align:left">商户自定义订单号</td>
<td style="text-align:left">string(32)</td>
<td style="text-align:left">商户可以通过 <code v-pre>order_id</code> 参数，查询本地数据库，确认用户是否支付成功，并给出相应的页面展示。</td>
</tr>
</tbody>
</table>
<div class="custom-container warning"><p class="custom-container-title">注意</p>
<p>请不要将此跳转作为用户支付成功的判断条件，此行为极不安全。请根据支付成功的回调通知是否送达，来判断用户是否支付成功。</p>
</div>
</div></template>


