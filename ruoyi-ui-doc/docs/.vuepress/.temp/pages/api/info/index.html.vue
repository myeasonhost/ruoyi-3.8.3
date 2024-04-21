<template><div><h2 id="一-平台账号" tabindex="-1"><a class="header-anchor" href="#一-平台账号"><span>一，平台账号：</span></a></h2>
<p>总后台：</p>
<p>https://tx8js.tx-line88.com/tx-admin/login?redirect=%2Fmember-manage%2Fmember-message-page</p>
<p>账号：eason/Eason123456</p>
<p>前端测试账号：</p>
<p>https://yh3627.com/</p>
<p>eason123/Eason1245------------------hgdeason123</p>
<p>注单后台：</p>
<p>http://18.166.37.217:8380/tx-data/login.html#sys/schedule.html
rolex
Aa@123456</p>
<p>https://txwluatadmin.tx88-info.com/tx-admin/</p>
<p>simeon、123456</p>
<p>Git账号1：http://192.168.102.201</p>
<p>hugo/Zz123321</p>
<p>Git账号2：http://192.168.105.110/</p>
<p>eason/Eason123456</p>
<p>天下OA：http://192.168.20.222/</p>
<p>eason/Eason123456</p>
<p>单语言 日志查询：
http://tx3.txwl-logs.com:5601
账号txwl  密码TX@123654</p>
<p>多语言 日志查询：
http://tx3-lang.txwl-logs.com:5601
账号txwl 密码TX@123654</p>
<h2 id="二-清除汇总" tabindex="-1"><a class="header-anchor" href="#二-清除汇总"><span>二，清除汇总</span></a></h2>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>SELECT * FROM  im_platform_report WHERE bet_time between '2024-03-01 00:00:00' and '2024-03-31 23:59:59';
delete FROM im_platform_report WHERE bet_time between '2024-03-01 00:00:00' and '2024-03-31 23:59:59';
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="三-清除返水" tabindex="-1"><a class="header-anchor" href="#三-清除返水"><span>三，清除返水</span></a></h2>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>#一级代理

delete from t_proxy_settlement where months >= '20240301' AND months &lt; '20240401';
delete from t_proxy_settlement_user_details where months >= '20240301' AND months &lt; '20240401';
delete from t_proxy_sum_settlement where months >= '20240301' AND months &lt; '20240401';

#二级代理

select count(1) from t_junior_proxy_user_settlement where months >= '20240301' and months &lt; '20240401';
delete from t_junior_proxy_user_settlement where months >= '20240301' and months &lt; '20240401';
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>redis: 密码: 123456
192.168.106.101:6379
192.168.106.101:6380
192.168.106.102:6379
192.168.106.102:6380
192.168.106.103:6379
192.168.106.103:6380</p>
<p>mongodb:
mongodb://myadmin:123456@192.168.106.101:27017,192.168.106.102:27017,192.168.106.103:27017/mcall</p>
<p>mp.zk.server-address:
192.168.106.101:2181,192.168.106.102:2181,192.168.106.103:2181</p>
<p>3台服务器密码: Aa123456, IP:
192.168.106.101
192.168.106.102
192.168.106.103</p>
<h2 id="四-服务安装" tabindex="-1"><a class="header-anchor" href="#四-服务安装"><span>四，服务安装</span></a></h2>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>docker run --restart=always --name mysql -v D:\docker\mysql8\cnf:/etc/mysql -v D:\docker\mysql8\data:/var/lib/mysql -v D:\docker\mysql8\log:/var/log -v D:\docker\mysql8\mysql-files:/var/lib/mysql-files -p 3306:3306 -e MYSQL_ROOT_PASSWORD=eason -d mysql:8.0.22

docker run --restart=always --name mysql5 -v D:\docker\mysql5\cnf:/etc/mysql -v D:\docker\mysql5\data:/var/lib/mysql -v D:\docker\mysql5\log:/var/log -v D:\docker\mysql5\mysql-files:/var/lib/mysql-files -p 3307:3306 -e MYSQL_ROOT_PASSWORD=Aa123456 -d mysql:5.7


docker run --restart=always --name mysql5 --privileged=true -v /var/docker/mysql5/logs:/logs -v /var/docker/mysql5/data:/var/lib/mysql -v /var/docker/mysql5/conf:/etc/mysql/conf.d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7

解决数据库不兼容的问题：

SELECT VERSION();

SHOW VARIABLES LIKE '%sql_mode'

SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>【MYSQL】配置安装</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>[mysqld]
#...其它配置内容
#以后要注意一些配置是否必须要放在mysqld后面才生效
#不区分大小写20231031补充，该命令必须放在mysqld下，否则不生效；
lower_case_table_names = 1
sql_mode = STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION


#设置group_concat限制长度
group_concat_max_len = 18446744073709551615
max_connection_errors = 1000

#设置字符集
character-set-client-handshake = FALSE
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
init_connect='SET NAMES utf8mb4'

[client]
default-character-set = utf8mb4
[mysql]
default-character-set = utf8mb4
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>【Mongo集群】配置安装</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>docker run --name mongodb --privileged=true -p 27017:27017 -d mongo --replSet "rs" 

var config={
	_id:"rs",
	members:[
		{_id:0,host:"192.168.106.101:27017",priority:3},
		{_id:1,host:"192.168.106.102:27017",priority:2},
		{_id:2,host:"192.168.106.103:27017",priority:1}
	]
};
rs.initiate(config)

use admin
db.createUser(
{
  user:"myadmin",
  pwd:"123456",
  roles:["root"]
 }
);

db.auth("myadmin", "admin");
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>【redis集群】配置安装</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>docker run --name redis-6380 --privileged=true -p 6380:6379 -v /var/docker/redis/6380/config/redis.conf:/etc/redis/redis.conf -v /var/docker/redis/6380/data:/data -d redis redis-server /etc/redis/redis.conf
docker run --name redis-6379 --privileged=true -p 6379:6379 -v /var/docker/redis/6379/config/redis.conf:/etc/redis/redis.conf -v /var/docker/redis/6379/data:/data -d redis redis-server /etc/redis/redis.conf

docker run --name redis-6379 --privileged=true -p 6379:6379 -v D:/docker/redis/cnf/redis.conf:/etc/redis/redis.conf -v D:/docker/redis/data:/data -d redis redis-server /etc/redis/redis.conf

docker exec -it redis-6379 /bin/bash
-->redis-cli
-->config set requirepass 123456

redis-cli -a 123456 --cluster create 192.168.106.101:6380 192.168.106.101:6379 192.168.106.102:6380 192.168.106.102:6379 192.168.106.103:6380 192.168.106.103:6379 --cluster-replicas 1
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code># 端口号
port 6379

# 设置客户端连接后进行任何其他指定前需要使用的密码
requirepass 123456

## 当master服务设置了密码保护时(用requirepass制定的密码)
# slave服务连接master的密码
masterauth 123456

# daemonize no 将daemonize yes注释起来或者 daemonize no设置，因为该配置和docker run中-d参数冲突，会导致容器一直启动失败
daemonize no

# 任何主机都可以连接到redis
bind 0.0.0.0

# 是否开启保护模式，默认开启。要是配置里没有指定bind和密码。开启该参数后，redis只会本地进行访问，拒绝外部访问。
protected-mode no

# 开启AOF
appendonly yes

# 集群开关，如果配置yes则开启集群功能，此redis实例作为集群的一个节点，否则，它是一个普通的单一的redis实例。
cluster-enabled yes

# 集群配置文件的名称，此配置文件不能人工编辑，它是集群节点自动维护的文件，主要用于记录集群中有哪些节点、他们的状态以及一些持久化参数等，方便在重启时恢复这些状态。通常是在收到请求之后这个文件就会被更新。
cluster-config-file nodes.conf

# 节点互连超时的阀值。集群节点超时毫秒数
cluster-node-timeout 15000

# 以下三个配置参数静态设定节点的运行参数, 包括对外IP, 普通Redis命令端口和集群桥接端口
# Redis Cluster 不支持NATted环境和IP地址或TCP端口被重映射(remapped)的环境。
# Docker使用一种名叫port mapping的技术, 运行于Docker容器内的程序实际对外使用的端口和监听的端口可不相同. 为了让Docker 兼容Redis Cluster, 需要使用Docker的host network模式。
#为了使Redis集群在这样的环境中工作，静态每个节点都知道需要其公共地址的配置
cluster-announce-ip 192.168.106.101

# 客户端连接端口
cluster-announce-port 6379

# 总线端口为普通端口port+10000，所谓Cluster bus, 即使用一种二进制协议(binary protocol)进行集群内点对点(node-to-node)通讯, 包括节点失效检测, 配置更新, 故障转移(failover)认证等
cluster-announce-bus-port 16379
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>【rabbitmq】配置安装</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>docker run --name rabbitmq --privileged=true -p 15672:15672 -p 5672:5672 --restart=always -e RABBITMQ_DEFAULT_VHOST=my_vhost -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -d rabbitmq:3.13.1-management

#延迟插件安装
rabbitmq-plugins enable  rabbitmq_delayed_message_exchange

</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="五-数据结构" tabindex="-1"><a class="header-anchor" href="#五-数据结构"><span>五，数据结构</span></a></h2>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>t_cagent 平台商表
t_cagent_storedvalue 平台商关联表

tx_agency_user 代理表
t_user	用户表
t_user_type 层级表
t_user_quantity  返水表
t_user_changer_login_password  密码更改记录
t_user_wallet 用户钱包表
t_user_tg TG用户表

t_cagent_ysepay 	支付平台商配置表
tx_online_deposit_record  订单表-存款
tx_withdraw_record		 提现表-取款
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>初始化用户基本信息：</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>&lt;!-- 初始化注册用户基本信息  -->
&lt;insert id="initRegisterUserInfo" parameterType="com.cn.tianxia.api.entity.manager.User">
INSERT INTO t_user_quantity (cid,uid,marking_quantity,user_quantity,winAmount,user_winAmount,user_quantity_history)
VALUES (#{cid},#{uid},0.00,0.00,0.00,0.00,0.00);
INSERT INTO t_user_wallet (uid,balance,frozen_balance,type,uptime)
VALUES (#{uid},0.00,0.00,'1',#{regDate});
INSERT INTO t_user_login (uid,login_time,login_ip,is_login,login_num,status,is_mobile,address,refurl)
VALUES (#{uid},#{regDate},INET_ATON(#{regIp}),1,1,1,#{isMobile},#{ipAddress},#{regurl});
&lt;/insert>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>MySQL版本控制：</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>SELECT @@global.sql_mode;

SET @@global.sql_mode="STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION"

</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>数据异常：</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>zya5669126932，zya6050804015，zya6430812208，zya6564712858
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><p>数据批量加速</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>List&lt;List&lt;AgencyUser>> parentUserList = CollUtil.split(agencyUserList, 1000);
CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(parentUserList.size());
parentUserList.forEach(subUserList -> {
    ThreadUtil.execute(() -> {
        try {
            agencyUserService.saveBatch(subUserList);
        } finally {
            countDownLatch.countDown();
        }
    });
});
countDownLatch.await();
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>支付编码</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>通道:          0微信，1支付宝，2 QQ，3银联，4京东(数字人民币)，5:快捷，6微信条码，7支付宝条码，8云闪付，9比特币   13网银
微信
{
"pc":"90,82,,,,,,,",
"mb":"90,82,,,,,,,",
"queryOrderUrl":"http://8.222.220.105/?c=pay&amp;a=query",
"payUrl":"http://8.222.220.105/?c=pay",
"secret":"ceac5161fa81f18239dab5e802a734c93c53498e",
"merchId":"pingguo123456",
"notifyUrl":"https://pay.gzjumeicom.com/callback/pay/notify/AMH/LIZI1",
"notifyIp":"8.222.220.105",
"changePayment":"LIZI:LIZI1",
}
支付宝
{
	"pc": ",8073,,,,,,,,,,,",
	"mb": ",8073,,,,,,,,,,,",
	"merchId": "20000479",
	"secret": "FFRKJFFXNIRR9LOEUVTGETMJXUDKCYZRC85TQ1UISUCYXTDFS17UOBDEQDSXHQNRQGNGHJX59ILTAW4BLCGTH5EAFV4FH9PST78O7ZOKUYQPXOQXXFSPMPTRJEOX779V",
	"payUrl": "http://119.8.97.199:3020/api/pay/create_order",
	"queryOrderUrl": "http://119.8.97.199:3020/api/pay/query_order",
	"notifyIp": "119.8.97.199",
	"changeNotifyB": "true",
	"notifyUrl": "http://pay.gzjumeicom.com/callback/pay/notify/AYH/MDAZF16",
	"changePayment": "HZZF:MDAZF16"
}
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>KQL语言</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>message : [LIZI] and 栗子支付 and ("ptype:83" or "ptype:82")
message : ([HZZF] and 合众支付) or ([LIZI] and 栗子支付)
message : ([HZZF] and 合众支付 and "productId:8073") or ([LIZI] and 栗子支付 and "ptype:83")
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="六-相关配置" tabindex="-1"><a class="header-anchor" href="#六-相关配置"><span>六，相关配置</span></a></h2>
<h3 id="_1-支付配置" tabindex="-1"><a class="header-anchor" href="#_1-支付配置"><span>1.支付配置</span></a></h3>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>AYH  迈达支付_P16    
编码：8073
支付宝纯原生H5
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>系统管理——&gt;数据字典（旧）</p>
<p><img src="@source/api/info/C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20240418200648463.png" alt="image-20240418200648463"></p>
<p>平台管理——&gt;在线支付配置管理</p>
<p><img src="@source/api/info/C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20240418200932105.png" alt="image-20240418200932105"></p>
<p>商家配置模版</p>
<div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre v-pre class="language-text"><code>支付信息配置
通道:          0微信，1支付宝，2 QQ，3银联，4京东(数字人民币)，5:快捷，6微信条码，7支付宝条码，8云闪付，9比特币   13网银

{
"pc":",,CUSTOM25,8129,,,,,,,,,",
"mb":"CUSTOM18,CUSTOM9,CUSTOM25,8129,,,,,,,,,",
"merchId":"YYHY",
"secret":"?dSRGLRILXX6D5ANLV4K",
"payUrl":"https://cs2u.xyz/req_topup.php",
"queryOrderUrl":"https://cs2u.xyz/check_ds_status.php",
"notifyIp":"220.158.208.225,220.158.208.226,103.4.6.226",
"notifyUrl":"http://pay.gzjumeicom.com/callback/pay/notify/YHY/CSZF1",
"changePayment":"CSZF:CSZF1"
}

{
	"pc": ",8073,,,,,,,,,,,",
	"mb": ",8073,,,,,,,,,,,",
	"merchId": "20000479",
	"secret": "FFRKJFFXNIRR9LOEUVTGETMJXUDKCYZRC85TQ1UISUCYXTDFS17UOBDEQDSXHQNRQGNGHJX59ILTAW4BLCGTH5EAFV4FH9PST78O7ZOKUYQPXOQXXFSPMPTRJEOX779V",
	"payUrl": "http://119.8.97.199:3020/api/pay/create_order",
	"queryOrderUrl": "http://119.8.97.199:3020/api/pay/query_order",
	"notifyIp": "119.8.97.199",
	"changeNotifyB": "true",
	"notifyUrl": "http://pay.gzjumeicom.com/callback/pay/notify/AYH/MDAZF16",
	"changePayment": "HZZF:MDAZF16"
}

日志扫描
message : ([HZZF] and 合众支付) or ([LIZI] and 栗子支付)

message:2024-04-18 20:55:40.773 - INFO [io-9098-exec-30] t.a.s.p.s.i.LIZIPayServiceImpl:65 : [LIZI]栗子支付扫码支付生成请求参数报文:{"client_ip":"117.28.246.3","format":"json","from_name":"from_name","goods_desc":"recharge","mch_id":"pingguo123456","money":"500.00","notify_url":"https://pay.gzjumeicom.com/callback/pay/notify/AMH/LIZI1","order_sn":"AMHlizi171344494077259270","pay_backurl":"http://m.g8184js.cc/","ptype":"82","sign":"c743b2d2116bd70c9422b9ca5b5e3e96","time":"1713444940"}

message:2024-04-18 20:55:43.098 - INFO [io-9098-exec-30] t.a.s.p.s.i.LIZIPayServiceImpl:67 : [LIZI]栗子支付响应信息: {"code":"1","msg":"下单成功","data":{"order_sn":"ZZHF20240418205540706","ptype":"82","ptype_name":"","realname":null,"account":null,"money":"500.00","bank":"","branch_name":"","qrcode":"http:\/\/gaofang418ssddi.yangguangalicloudapi.xyz\/alipayCodeImg.php?is_bzk=0&amp;account_name=LJo0t8zC5eTVxrLYEmtIwg%3D%3D&amp;bank_name=LJo0t8zC5eTVxrLYEmtIwg%3D%3D&amp;account_number=wz4a1r0COFdj%2FZgu5vSsXdFGA%2FFUhCmA2o4TGfJrS2AbjQ1%2BsiIkciad1HLCelAfHV3IGR6vtjfNJDCJpmCs9oKjf4mCiU3XjiOy9PmJ6dEdree7mP%2BwtCFPtoq59Ety&amp;trade_no=P1780943284685848577&amp;order_pay_price=500.10&amp;sign=4e031f1b0e698a899dee6d0fc5d3add4&amp;user=ylHp4h7CQJqPVWjN6GD%2BsK}

message:2024-04-18 21:02:16.510 - INFO [io-9095-exec-13] t.a.s.p.s.i.HZZFPayServiceImpl:70 : [HZZF]合众支付扫码支付生成请求参数报文:{"mchId":"20000479","productId":"8063","mchOrderNo":"AYHhzzf171344533650717960","amount":"30000","currency":"cny","notifyUrl":"http://pay.gzjumeicom.com/callback/pay/notify/AYH/MDAZF8","subject":"xxpay测试商品1","body":"xxpay测试商品描述","reqTime":"20240418210216","version":"1.0","sign":"DC3E0D47A8F60EAAC11A79CB72A9FD86"}

message:2024-04-18 21:02:16.767 - INFO [io-9095-exec-13] t.a.s.p.s.i.HZZFPayServiceImpl:72 : [HZZF]合众支付响应信息:{"retCode":"0","sign":"D9315B1D114C9065BE551D6238743391","payOrderId":"P01202404182102166677125","payUrl":"http://pay.xypglgf.com/u7bn2d/api/pay?order_no=2024041821021633675587169","payUrl2":"http://pay.xypglgf.com/u7bn2d/api/pay?order_no=2024041821021633675587169","payJumpUrl":"&lt;html> &lt;script>document.location.href='http://pay.xypglgf.com/u7bn2d/api/pay?order_no=2024041821021633675587169'&lt;/script>&lt;/html>"}

</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>日志扫描</p>
<h3 id="_2-补钱配置" tabindex="-1"><a class="header-anchor" href="#_2-补钱配置"><span>2.补钱配置</span></a></h3>
</div></template>


