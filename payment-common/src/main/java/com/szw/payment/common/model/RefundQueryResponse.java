package com.szw.payment.common.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RefundQueryResponse {
	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 外部订单号
	 */
	private String outTradeNo;

	/**
	 * 平台退款号
	 */
	private String outRefundNo;

	/**
	 * 退款状态
	 */
	private String refundStatus;

	/**
	 * 退款完成时间 如果返回Null 使用系统当前时间
	 */
	private LocalDateTime refundDoneTime;

}
