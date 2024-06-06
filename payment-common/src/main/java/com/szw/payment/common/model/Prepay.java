package com.szw.payment.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Prepay {

	/**
	 * 商家订单id
	 */
	private String outTradeNo;

	/**
	 * 商品描述
	 */
	private String body;

	/**
	 * 商品详情
	 */
	private String detail;

	/**
	 * 标价币种
	 */
	private String feeType;

	/**
	 * 支付金额
	 */
	private Long totalFee;

	/**
	 * 用户标识 微信小程序支付必传
	 */
	private String openId;

	/**
	 * 失效时间
	 */
	private LocalDateTime expireTime;

	/**
	 * 回传参数
	 */
	private String passBackParams;

	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 平台订单id
	 */
	private String tradeId;

	/**
	 * 平台用户id
	 */
	private String userId;

}
