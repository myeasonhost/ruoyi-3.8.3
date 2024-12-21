<template><div><h1 id="收款地址生成-替换" tabindex="-1"><a class="header-anchor" href="#收款地址生成-替换"><span>收款地址生成 &amp; 替换</span></a></h1>
<div class="custom-container tip"><p class="custom-container-title">说明</p>
<p>商户可调用该接口，生成一个全新未使用的波场钱包地址。同时该操作会使用新生成的地址，替换当前已启用的收款地址。</p>
</div>
<div class="custom-container danger"><p class="custom-container-title">警告</p>
<p>该接口返回的数据是一次性的，请务必妥善保管相关信息，否则可能会造成不必要的资金损失。</p>
</div>
<h2 id="接口地址" tabindex="-1"><a class="header-anchor" href="#接口地址"><span>接口地址</span></a></h2>
<div class="language-bash" data-highlighter="prismjs" data-ext="sh" data-title="sh"><pre v-pre class="language-bash"><code><span class="line">POST https://pro.tronapi.com/api/address/generate_add</span>
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
<span class="line">    <span class="token property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span></span>
<span class="line">        <span class="token property">"address"</span><span class="token operator">:</span> <span class="token punctuation">{</span></span>
<span class="line">            <span class="token property">"base58"</span><span class="token operator">:</span> <span class="token string">"TWsqYHJt97XcQ1m2FBgjSTmVkCy5vZWMNi"</span><span class="token punctuation">,</span></span>
<span class="line">            <span class="token property">"hex"</span><span class="token operator">:</span> <span class="token string">"41E556A5771684C967C66AE3CAA047A017A90E3775"</span></span>
<span class="line">        <span class="token punctuation">}</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"privateKey"</span><span class="token operator">:</span> <span class="token string">"0189BBADCA8B8BBB8B97F920BA3472C4A0F4A56371E6CAFCC82F32DDF210AB24"</span><span class="token punctuation">,</span></span>
<span class="line">        <span class="token property">"publicKey"</span><span class="token operator">:</span> <span class="token string">"04B1384AE0887DEAAE3C35E4A36E489A81E7A93547A041CBD306E3FD935A2757EA394E65051E60F6FB625C3D9BCC7B57544BFDE6BB5F49E5DB6ED665E9AC98F6B3"</span></span>
<span class="line">    <span class="token punctuation">}</span><span class="token punctuation">,</span></span>
<span class="line">    <span class="token property">"success"</span><span class="token operator">:</span> <span class="token boolean">true</span></span>
<span class="line"><span class="token punctuation">}</span></span>
<span class="line"></span></code></pre>
</div></div></template>


