/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.22 : Database - tron_wallet
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tron_wallet` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tron_wallet`;

/*Table structure for table `org_account_address` */

DROP TABLE IF EXISTS `org_account_address`;

CREATE TABLE `org_account_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `address_type` varchar(64) NOT NULL COMMENT '地址类型',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户地址',
  `privateKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '私钥',
  `balance` varchar(64) DEFAULT NULL COMMENT '余额集合',
  `total_amount` decimal(18,2) DEFAULT '0.00' COMMENT '累计收款金额',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `remark` varchar(64) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `address` (`address`,`agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户地址表';

/*Table structure for table `org_account_info` */

DROP TABLE IF EXISTS `org_account_info`;

CREATE TABLE `org_account_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '序号',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID（绑定系统表）',
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID（用户名）',
  `point` double NOT NULL COMMENT '占比',
  `service_charge` double NOT NULL COMMENT '服务费',
  `min` double DEFAULT NULL COMMENT '最低消费额',
  `white_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '白名单',
  `google_secret_code` varchar(64) NOT NULL COMMENT '谷歌秘钥',
  `google_secret_qrurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '谷歌秘钥二维码',
  `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回调通知地址',
  `tgbot_group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '飞机机器人群ID',
  `private_key` varchar(64) NOT NULL COMMENT '私钥',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_name` (`agency_id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商户信息表';

/*Table structure for table `org_account_order` */

DROP TABLE IF EXISTS `org_account_order`;

CREATE TABLE `org_account_order` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付订单号',
  `site_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户ID',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户订单号',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名（用户ID）',
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名',
  `amount` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '金额',
  `currency` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'USD' COMMENT '订单币种（CNY/USD）',
  `coin_amount` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付金额（订单金额 + 订单币种汇率）',
  `coin_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'USDT' COMMENT '支付币种（USDT/RMB）',
  `coin_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收款地址',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '1=支付中,2=支付成功，3=支付超时',
  `notify_succeed` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '0=未通知，1=通知成功，2=通知失败',
  `notify_times` int DEFAULT '0' COMMENT '通知次数',
  `last_notify_time` datetime DEFAULT NULL COMMENT '最后通知时间',
  `next_notify_time` datetime DEFAULT NULL COMMENT '下一次通知时间',
  `timeout` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单过期时间（分钟）',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `notify_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调通知地址',
  `redirect_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '同步跳转地址',
  `cashier_url` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '官方收银台地址',
  `qrcode_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收款码地址',
  `transaction_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易事务ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `expiration_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `site_pay_order_id` (`site_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商户订单表';

/*Table structure for table `org_account_order_daip` */

DROP TABLE IF EXISTS `org_account_order_daip`;

CREATE TABLE `org_account_order_daip` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付订单号',
  `site_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户ID',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户订单号',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名（用户ID）',
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名',
  `amount` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '金额',
  `currency` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'USD' COMMENT '订单币种（CNY/USD）',
  `coin_amount` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付金额（订单金额 + 订单币种汇率）',
  `coin_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'USDT' COMMENT '支付币种（USDT/RMB）',
  `out_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转出地址',
  `coin_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收款地址',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '1=提现中,2=提现成功，3=拒绝提现，4=转账异常',
  `notify_succeed` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '0=未通知，1=通知成功，2=通知失败',
  `notify_times` int DEFAULT '0' COMMENT '通知次数',
  `last_notify_time` datetime DEFAULT NULL COMMENT '最后通知时间',
  `next_notify_time` datetime DEFAULT NULL COMMENT '下一次通知时间',
  `pay_time` datetime DEFAULT NULL COMMENT '转账时间',
  `notify_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调通知地址',
  `transaction_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易事务ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `site_pay_order_id` (`site_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商户代付表';

/*Table structure for table `tron_account_address` */

DROP TABLE IF EXISTS `tron_account_address`;

CREATE TABLE `tron_account_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `address_type` varchar(64) NOT NULL COMMENT '地址类型',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权地址-Base58格式',
  `hex_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权地址-Hex格式',
  `private_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '私钥',
  `balance` varchar(64) DEFAULT NULL COMMENT '余额集合',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `remark` varchar(64) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `address` (`address`,`agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='站内账号表';

/*Table structure for table `tron_auth_address` */

DROP TABLE IF EXISTS `tron_auth_address`;

CREATE TABLE `tron_auth_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) NOT NULL COMMENT '业务员ID',
  `address_type` varchar(64) NOT NULL COMMENT '地址类型',
  `au_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权地址-Base58格式',
  `au_hex_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权地址-Hex格式',
  `url_address` varchar(64) DEFAULT NULL COMMENT '生成地址',
  `privateKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '私钥',
  `balance` varchar(64) DEFAULT NULL COMMENT '余额集合',
  `token` varchar(64) NOT NULL COMMENT '授权代码',
  `saleman_phone` varchar(64) DEFAULT NULL COMMENT '客服电话号码',
  `remark` varchar(64) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `au_address` (`au_address`) USING BTREE,
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='授权表';

/*Table structure for table `tron_auth_record` */

DROP TABLE IF EXISTS `tron_auth_record`;

CREATE TABLE `tron_auth_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `au_id` bigint NOT NULL COMMENT '授权ID',
  `agency_id` varchar(32) DEFAULT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) NOT NULL COMMENT '业务员ID',
  `token` varchar(32) NOT NULL COMMENT '授权Token',
  `address` varchar(64) NOT NULL COMMENT '地址',
  `au_address` varchar(64) NOT NULL COMMENT '授权地址',
  `ip` varchar(64) NOT NULL COMMENT '账户IP地址',
  `area` varchar(255) DEFAULT NULL COMMENT '地区',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `address` (`address`),
  KEY `fk_au_id` (`au_id`),
  CONSTRAINT `fk_au_id` FOREIGN KEY (`au_id`) REFERENCES `tron_auth_address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='授权记录表';

/*Table structure for table `tron_bill_record` */

DROP TABLE IF EXISTS `tron_bill_record`;

CREATE TABLE `tron_bill_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) DEFAULT NULL COMMENT '业务员ID',
  `type` varchar(32) NOT NULL DEFAULT 'TRX' COMMENT '转化类型',
  `from_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源地址',
  `au_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权地址',
  `to_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收账户',
  `bill_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算账户',
  `withdraw_balance` double DEFAULT NULL COMMENT '提现金额',
  `bill_balance` double DEFAULT NULL COMMENT '结算金额',
  `service_charge` double DEFAULT NULL COMMENT '手续费',
  `finish_balance` double DEFAULT NULL COMMENT '最终金额',
  `status` varchar(10) DEFAULT NULL COMMENT '1=广播中,2=广播成功，3=广播失败，4=交易成功，5=交易失败',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='结算记录表';

/*Table structure for table `tron_eason_address` */

DROP TABLE IF EXISTS `tron_eason_address`;

CREATE TABLE `tron_eason_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `address_type` varchar(64) NOT NULL COMMENT '地址类型',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址-Base58格式',
  `hex_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址-Hex格式',
  `privateKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '私钥',
  `balance` varchar(64) DEFAULT NULL COMMENT '余额',
  `point` double NOT NULL COMMENT '占比',
  `service_charge` double NOT NULL COMMENT '服务费',
  `min` double DEFAULT NULL COMMENT '最低消费额',
  `status` varchar(10) DEFAULT NULL COMMENT '0=启用，1=禁用',
  `remark` varchar(64) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `address` (`address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='总站账户表';

/*Table structure for table `tron_fans` */

DROP TABLE IF EXISTS `tron_fans`;

CREATE TABLE `tron_fans` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) NOT NULL COMMENT '业务员ID',
  `mobile` varchar(64) NOT NULL COMMENT '电话',
  `chanel` varchar(64) DEFAULT NULL COMMENT '渠道',
  `profession` varchar(64) DEFAULT NULL COMMENT '职业',
  `wallet_type` varchar(64) DEFAULT NULL COMMENT '钱包类型',
  `exchange_type` varchar(64) DEFAULT NULL COMMENT '交易所类型',
  `area` varchar(64) DEFAULT NULL COMMENT '地区',
  `status` varchar(10) DEFAULT NULL COMMENT '0=有效，1=无效',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_moblie` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='粉丝表';

/*Table structure for table `tron_fish` */

DROP TABLE IF EXISTS `tron_fish`;

CREATE TABLE `tron_fish` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) NOT NULL COMMENT '业务员ID',
  `au_record_id` bigint DEFAULT NULL,
  `address` varchar(64) NOT NULL COMMENT '地址',
  `au_address` varchar(64) DEFAULT NULL COMMENT '授权地址',
  `balance` json DEFAULT NULL COMMENT '余额',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'TRX' COMMENT '类型',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `mobile` varchar(64) DEFAULT NULL COMMENT '电话',
  `area` varchar(64) DEFAULT NULL COMMENT '地区',
  `is_top` varchar(10) DEFAULT '0' COMMENT '是否置顶',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_au_record_id` (`au_record_id`),
  CONSTRAINT `fk_au_record_id` FOREIGN KEY (`au_record_id`) REFERENCES `tron_auth_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='鱼苗表';

/*Table structure for table `tron_image_config01` */

DROP TABLE IF EXISTS `tron_image_config01`;

CREATE TABLE `tron_image_config01` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) NOT NULL COMMENT '业务员ID',
  `xinhao` varchar(64) NOT NULL COMMENT '信号',
  `wang` varchar(64) NOT NULL COMMENT '网络',
  `dian` varchar(64) NOT NULL COMMENT '电量',
  `time` varchar(64) NOT NULL COMMENT '当前时间',
  `address` varchar(64) DEFAULT NULL COMMENT '钱包地址',
  `noread_tansfer` varchar(64) DEFAULT NULL COMMENT '转账未读',
  `noread_system` varchar(10) DEFAULT NULL COMMENT '系统未读',
  `trx_price` varchar(64) DEFAULT NULL COMMENT 'trx价格',
  `trx_num` varchar(64) DEFAULT NULL COMMENT 'trx数量',
  `usdt_price` varchar(64) DEFAULT NULL COMMENT 'usdt价格',
  `usdt_num` varchar(64) DEFAULT NULL COMMENT 'usdt价格',
  `shouyi` varchar(64) DEFAULT NULL COMMENT '总收益',
  `tixian` varchar(64) DEFAULT NULL COMMENT '待提现',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_id` (`saleman_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图片配置表01';

/*Table structure for table `tron_image_config02` */

DROP TABLE IF EXISTS `tron_image_config02`;

CREATE TABLE `tron_image_config02` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) NOT NULL COMMENT '业务员ID',
  `config_id` bigint DEFAULT NULL COMMENT '配置ID',
  `opt_time` varchar(64) NOT NULL COMMENT '发生时间',
  `tranfer_type` varchar(64) NOT NULL COMMENT '类型',
  `coin_type` varchar(64) NOT NULL COMMENT '币种',
  `address` varchar(64) NOT NULL COMMENT '钱包地址',
  `num` varchar(64) NOT NULL COMMENT '数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图片配置表02';

/*Table structure for table `tron_interest_record` */

DROP TABLE IF EXISTS `tron_interest_record`;

CREATE TABLE `tron_interest_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fish_id` bigint NOT NULL COMMENT '鱼苗ID',
  `agency_id` varchar(64) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(64) NOT NULL COMMENT '业务员ID',
  `address` varchar(64) DEFAULT NULL COMMENT '客户地址',
  `current_balance` double DEFAULT NULL COMMENT '当前本金',
  `change_balance` double DEFAULT NULL COMMENT '变动金额',
  `current_interest` double DEFAULT NULL COMMENT '当前利息',
  `status` varchar(10) NOT NULL COMMENT '1=审核中,2=已登记，3=已打息',
  `remark` varchar(64) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_fish_id` (`fish_id`),
  CONSTRAINT `fk_fish_id` FOREIGN KEY (`fish_id`) REFERENCES `tron_fish` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=326 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='利息表';

/*Table structure for table `tron_tansfer_record` */

DROP TABLE IF EXISTS `tron_tansfer_record`;

CREATE TABLE `tron_tansfer_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(32) DEFAULT NULL COMMENT '业务员ID',
  `from_address` varchar(64) NOT NULL COMMENT '来源地址',
  `to_address` varchar(64) NOT NULL COMMENT '接收账户',
  `balance` double DEFAULT NULL COMMENT '交易金额',
  `address_type` varchar(64) DEFAULT NULL COMMENT '地址类型',
  `type` varchar(64) NOT NULL COMMENT '1=赠送,2=打息,3=转账',
  `status` varchar(10) NOT NULL COMMENT '1=广播中,2=广播成功，3=广播失败',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='转账记录表';

/*Table structure for table `tron_web_config` */

DROP TABLE IF EXISTS `tron_web_config`;

CREATE TABLE `tron_web_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agency_id` varchar(32) NOT NULL COMMENT '代理ID',
  `total_output` double DEFAULT NULL COMMENT '总矿池',
  `valid_node` double DEFAULT NULL COMMENT '节点',
  `participant` double DEFAULT NULL COMMENT '参与者人数',
  `user_revenue` double DEFAULT NULL COMMENT '用户收入',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_agent_id` (`agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='矿机设置表';

/*Table structure for table `tron_withdraw_record` */

DROP TABLE IF EXISTS `tron_withdraw_record`;

CREATE TABLE `tron_withdraw_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fish_id` bigint NOT NULL COMMENT '鱼苗ID',
  `agency_id` varchar(64) NOT NULL COMMENT '代理ID',
  `saleman_id` varchar(64) NOT NULL COMMENT '业务员ID',
  `address` varchar(64) NOT NULL COMMENT '客户地址',
  `current_balance` double DEFAULT NULL COMMENT '当前本金',
  `total_balance` double DEFAULT NULL COMMENT '总金额',
  `current_withdraw` double DEFAULT NULL COMMENT '当前提款',
  `status` varchar(10) NOT NULL COMMENT '1=审核中,2=同意提现，3=打款已提，4=拒绝提现',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_withdraw_fish_id` (`fish_id`),
  CONSTRAINT `fk_withdraw_fish_id` FOREIGN KEY (`fish_id`) REFERENCES `tron_fish` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='提款表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
