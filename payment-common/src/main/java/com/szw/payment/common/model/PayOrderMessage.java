package com.szw.payment.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayOrderMessage implements Serializable {
	@Serial
	private static final long serialVersionUID = 4680725040087814445L;

	/**
	 * 支付渠道
	 */
	private String channel;

	/**
	 * 支付平台返回预支付id
	 */
	private String prePayId;

	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 支付完成时间
	 */
	private LocalDateTime payDoneTime;

	/**
	 * 平台订单id
	 */
	private String tradeId;

	/**
	 * 平台用户id
	 */
	private String userId;

}
