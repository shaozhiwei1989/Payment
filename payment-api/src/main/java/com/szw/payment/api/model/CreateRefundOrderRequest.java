package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CreateRefundOrderRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -6825991801814132428L;

	/**
	 * 平台订单id
	 */
	private String tradeId;

	/**
	 * 平台用户id
	 */
	private String userId;

	/**
	 * 支付渠道
	 */
	private String channel;

	/**
	 * 幂等key 外部传入
	 * 要求全局唯一，建议使用业务系统标识 + 业务数据唯一键
	 */
	private String idempotentKey;

	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 退款金额
	 */
	private Long amount;

	/**
	 * 退款描述
	 */
	private String description;

	/**
	 * 回传参数
	 */
	private Map<String, String> passBackParamMap = new HashMap<>();

}
