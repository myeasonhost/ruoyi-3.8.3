<template><div><h1 id="_2-2-订单回调" tabindex="-1"><a class="header-anchor" href="#_2-2-订单回调"><span>2.2 订单回调</span></a></h1>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>用户支付完成后，系统会自动向订单关联的回调地址（<code v-pre>notify_url</code>）发送通知消息，告知该笔订单已支付完成。</p>
</div>
<h2 id="回调地址" tabindex="-1"><a class="header-anchor" href="#回调地址"><span>回调地址</span></a></h2>
<div class="language-bash" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code>POST notify_url（商户在订单创建接口中指定）
</code></pre></div><h2 id="通知参数" tabindex="-1"><a class="header-anchor" href="#通知参数"><span>通知参数</span></a></h2>
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
<td style="text-align:left">pay_id</td>
<td style="text-align:left">官方订单号</td>
<td style="text-align:left">string(32)</td>
<td style="text-align:left">MBUSDT 支付 ID。</td>
</tr>
<tr>
<td style="text-align:left">order_id</td>
<td style="text-align:left">商讨自定义订单号</td>
<td style="text-align:left">string(64)</td>
<td style="text-align:left">商户自定义订单号，原样返回。</td>
</tr>
<tr>
<td style="text-align:left">amount</td>
<td style="text-align:left">订单金额</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">商户发起接口请求时指定的 <code v-pre>订单金额</code>。</td>
</tr>
<tr>
<td style="text-align:left">currency</td>
<td style="text-align:left">订单货币单位</td>
<td style="text-align:left">string(8)</td>
<td style="text-align:left">商户发起接口请求时指定的 <code v-pre>订单币种</code>，固定为 <code v-pre>USDT</code>。</td>
</tr>
<tr>
<td style="text-align:left">coin_code</td>
<td style="text-align:left">订单支付币种</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">商户发起接口请求时指定的 <code v-pre>支付币种</code></td>
</tr>
<tr>
<td style="text-align:left">coin_amount</td>
<td style="text-align:left">订单支付金额</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">订单显示的支付金额，由 <code v-pre>订单金额</code> + <code v-pre>订单币种</code> + <code v-pre>支付币种</code> 按照实时行情转换而来。</td>
</tr>
<tr>
<td style="text-align:left">pay_time</td>
<td style="text-align:left">订单支付时间</td>
<td style="text-align:left">Long</td>
<td style="text-align:left">支付时间戳。</td>
</tr>
<tr>
<td style="text-align:left">status</td>
<td style="text-align:left">订单状态</td>
<td style="text-align:left">string(16)</td>
<td style="text-align:left">代收订单状态，<code v-pre>1=支付中,2=支付成功，3=支付超时</code></td>
</tr>
<tr>
<td style="text-align:left">hash</td>
<td style="text-align:left">交易 hash</td>
<td style="text-align:left">string(128)</td>
<td style="text-align:left">区块链交易 hash。商户可打开区块链浏览器查询交易详情。</td>
</tr>
<tr>
<td style="text-align:left">sign</td>
<td style="text-align:left">签名串</td>
<td style="text-align:left">string(32)</td>
<td style="text-align:left">安全校验签名串。</td>
</tr>
</tbody>
</table>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>参数名 ASCII 码从小到大排序（字典序）<br>
<code v-pre>sign</code> 的生成规则为：<code v-pre>toLowerCase(md5(pay_id + order_id + amount + currency + coin_code + coin_amount + pay_time + hash + api key))</code>。</p>
</div>
<h2 id="通知示例" tabindex="-1"><a class="header-anchor" href="#通知示例"><span>通知示例</span></a></h2>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>amount=1&amp;coin_amount=10&amp;coin_code=USDT&amp;currency=USD&amp;hash=32421c1dd697913b31320be41da74a82b1bc45535daf7e08d24e0140b947f1ec
&amp;order_id=1&amp;pay_id=MB1626960689799114752&amp;pay_time=1675352103000&amp;status=2&amp;sign=400152aa1a1d3002552585e1849daba9
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="通知返回" tabindex="-1"><a class="header-anchor" href="#通知返回"><span>通知返回</span></a></h2>
<p>商户在收到通知信息后，可返回以下内容，告知已收到回调通知：</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>SUCCESS
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><h2 id="通知重试" tabindex="-1"><a class="header-anchor" href="#通知重试"><span>通知重试</span></a></h2>
<p>系统向商户创建订单时指定的 <code v-pre>notify_url</code> 发送 <code v-pre>回调通知</code> 后，如该 <code v-pre>notify_url</code> 回调返回的返回值不为 <code v-pre>SUCCESS</code>，则系统会触发 <code v-pre>重试机制</code>。相关规则如下：</p>
<ol>
<li>第一次通知：距离第一次10秒钟;。</li>
<li>第二次通知：距离第二次1分钟。</li>
<li>第三次通知: 距离第三次5分钟。</li>
<li>三次通知不成功，请通知商户排查原因，则需要进行商户后台手动通知</li>
</ol>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>商户也可在后台 <code v-pre>支付订单</code> 页面，动手触发 <code v-pre>回调通知</code>。</p>
</div>
</div></template>


