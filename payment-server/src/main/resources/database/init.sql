create database payment;
use payment;

CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `channel` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '支付渠道',
  `app_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信支付：商户号绑定的appID\n支付宝支付：支付宝商家应用ID',
  `mch_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付商户号 微信支付专用',
  `mch_serial_number` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商户号证书序列号 微信支付专用',
  `api_key` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'api密钥',
  `public_key` text COLLATE utf8mb4_bin COMMENT '公钥',
  `private_key` text COLLATE utf8mb4_bin COMMENT '私钥',
  `notify_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付回调地址',
  `refund_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退款回调地址',
  `api_version` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'api版本',
  `description` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `is_deleted` char(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_channel` (`channel`) USING BTREE,
  KEY `idx_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `pay_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `out_trade_no` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '向支付平台申请支付单时使用的商家订单id\n全局唯一',
  `trade_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '平台订单id',
  `user_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '平台用户id',
  `config_id` bigint(20) NOT NULL COMMENT '支付配置id',
  `channel` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '支付渠道',
  `pre_pay_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付平台返回预支付id',
  `transaction_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付流水号',
  `amount` bigint(20) NOT NULL COMMENT '支付单支付金额',
  `balance` bigint(20) NOT NULL COMMENT '余额（支付金额 - 退款金额 - 冻结金额）',
  `refund_frozen_amount` bigint(20) NOT NULL COMMENT '退款冻结金额',
  `refund_amount` bigint(20) NOT NULL COMMENT '已经退款金额',
  `status` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '状态',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `pay_done_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `extra_param` text COLLATE utf8mb4_bin COMMENT '扩展字段',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_trade_no` (`out_trade_no`),
  UNIQUE KEY `uk_trade_id_channel` (`trade_id`,`channel`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `refund_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_id` bigint(20) NOT NULL COMMENT '支付单配置id',
  `pay_order_id` bigint(20) NOT NULL COMMENT '支付单id',
  `user_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '平台用户id',
  `trade_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '平台订单id',
  `out_refund_no` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT 'api外部退款号，平台唯一',
  `amount` int(11) NOT NULL COMMENT '退款金额',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `idempotent_key` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '幂等key 外部传入',
  `description` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退款原因描述',
  `refund_create_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `refund_end_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `refund_fail_desc` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退款失败描述',
  `retries` int(11) NOT NULL DEFAULT '0' COMMENT '重试次数',
  `pass_back_param` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '回传参数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_refund_no` (`out_refund_no`),
  UNIQUE KEY `uk_idempotent_key` (`idempotent_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `refund_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `refund_order_id` bigint(20) NOT NULL COMMENT '退款单id',
  `trade_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台订单id',
  `user_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台用户id',
  `exec_time` datetime DEFAULT NULL COMMENT '执行时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_order_id` (`refund_order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
