<template><div><h1 id="收款地址查询" tabindex="-1"><a class="header-anchor" href="#收款地址查询"><span>收款地址查询</span></a></h1>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>商户可调用该接口，查询已配置的收款地址信息。</p>
</div>
<h2 id="接口地址" tabindex="-1"><a class="header-anchor" href="#接口地址"><span>接口地址</span></a></h2>
<div class="language-bash" data-highlighter="prismjs" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code><span class="line">GET https://pro.tronapi.com/api/address/list</span>
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
<td style="text-align:left">public_key</td>
<td style="text-align:left">public key</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(32)</td>
<td style="text-align:left">商户 public key。</td>
</tr>
<tr>
<td style="text-align:left">signature</td>
<td style="text-align:left">签名串</td>
<td style="text-align:left">必填</td>
<td style="text-align:left">string(32)</td>
<td style="text-align:left">安全校验签名串。</td>
</tr>
</tbody>
</table>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p><code v-pre>signature</code> 的生成规则为：<code v-pre>toLowerCase(md5(public key + private key))</code>。</p>
</div>
<h2 id="返回示例" tabindex="-1"><a class="header-anchor" href="#返回示例"><span>返回示例</span></a></h2>
<div class="language-json" data-highlighter="prismjs" data-ext="json" data-title="json"><pre v-pre class="language-json"><code><span class="line"><span class="token punctuation">{</span></span>
<span class="line">    <span class="token property">"code"</span><span class="token operator">:</span> <span class="token number">200</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"data"</span><span class="token operator">:</span> <span class="token punctuation">[</span></span>
<span class="line">        <span class="token punctuation">{</span></span>
<span class="line">            <span class="token property">"address"</span><span class="token operator">:</span> <span class="token string">"TNM78UYBFdMBoo5rgZvZpr28kxqRtDxMH8"</span><span class="token punctuation">,</span></span>
<span class="line">            <span class="token property">"create_time"</span><span class="token operator">:</span> <span class="token string">"2022-04-10 15:43:33"</span><span class="token punctuation">,</span></span>
<span class="line">            <span class="token property">"enabled"</span><span class="token operator">:</span> <span class="token boolean">true</span><span class="token punctuation">,</span></span>
<span class="line">            <span class="token property">"receive_amount"</span><span class="token operator">:</span> <span class="token string">"15.7"</span><span class="token punctuation">,</span></span>
<span class="line">            <span class="token property">"receive_latest"</span><span class="token operator">:</span> <span class="token string">"2022-04-11 15:43:33"</span></span>
<span class="line">        <span class="token punctuation">}</span></span>
<span class="line">    <span class="token punctuation">]</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"success"</span><span class="token operator">:</span> <span class="token boolean">true</span></span>
<span class="line"><span class="token punctuation">}</span></span>
<span class="line"></span></code></pre>
</div></div></template>


