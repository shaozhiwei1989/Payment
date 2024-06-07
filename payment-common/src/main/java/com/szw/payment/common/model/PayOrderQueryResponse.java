package com.szw.payment.common.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PayOrderQueryResponse {

	/**
	 * 外部订单号
	 */
	private String outTradeNo;

	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 支付完成时间
	 */
	private LocalDateTime payDoneTime;

	/**
	 * 支付平台订单状态
	 */
	private String tradeState;

}
