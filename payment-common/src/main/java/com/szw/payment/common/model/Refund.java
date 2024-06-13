package com.szw.payment.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Refund {
	/**
	 * 平台退款号（接口请求号）
	 */
	private String outRefundNo;

	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 商家平台订单id
	 */
	private String outTradeNo;

	/**
	 * 订单总金额
	 */
	private Long totalFee;

	/**
	 * 退款金额
	 */
	private Long refundFee;

	/**
	 * 退款描述
	 */
	private String refundDesc;

	/**
	 * 幂等key 外部传入
	 */
	private String idempotentKey;

	/**
	 * 回传参数
	 */
	private String passBackParam;

}
