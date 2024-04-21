<template><div><h1 id="_1-2-安全说明" tabindex="-1"><a class="header-anchor" href="#_1-2-安全说明"><span>1.2 安全说明</span></a></h1>
<h2 id="密钥" tabindex="-1"><a class="header-anchor" href="#密钥"><span>密钥</span></a></h2>
<p>商户的 <code v-pre>api key</code> 非常重要，请勿显示在任何网页代码、网址参数中。</p>
<h2 id="钱包私钥" tabindex="-1"><a class="header-anchor" href="#钱包私钥"><span>钱包私钥</span></a></h2>
<p>请务必妥善保管钱包的私钥（<code v-pre>privateKey</code>），否则可能导致意外的资金损失。</p>
<h2 id="signature" tabindex="-1"><a class="header-anchor" href="#signature"><span>signature</span></a></h2>
<p>为保障接口安全，系统端会对接收到的所有数据，进行 <code v-pre>signature</code> 匹配校验，建议商户端包含同样的校验逻辑，防止意外的安全风险。</p>
<h2 id="账户安全" tabindex="-1"><a class="header-anchor" href="#账户安全"><span>账户安全</span></a></h2>
<p>建议商户开启基于 <code v-pre>Google authenticator</code> 的两步登录安全验证，防范因账密泄漏导致的资产安全问题。</p>
<h2 id="接口安全" tabindex="-1"><a class="header-anchor" href="#接口安全"><span>接口安全</span></a></h2>
<p>商户可在系统后台配置 <code v-pre>接口IP白名单</code>，防范因密钥泄漏导致的资产安全问题。</p>
<h2 id="https" tabindex="-1"><a class="header-anchor" href="#https"><span>HTTPS</span></a></h2>
<p>部分接口需要提供 <code v-pre>notify_url</code> &amp; <code v-pre>redirect_url</code>，建议商户对服务器部署 <code v-pre>https</code> 服务，保障数据的通信安全。</p>
</div></template>


