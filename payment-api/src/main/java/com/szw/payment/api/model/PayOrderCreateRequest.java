package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Valid
public class PayOrderCreateRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = 4335789550292369650L;

	/**
	 * 支付渠道
	 */
	@NotNull(message = "支付渠道不能为空")
	private String channel;

	/**
	 * 商品描述
	 */
	private String body;

	/**
	 * 支付金额
	 */
	private Long totalFee;

	/**
	 * 平台订单id
	 */
	private String tradeId;

	/**
	 * 平台用户id
	 */
	private String userId;

	/**
	 * 用户标识 微信小程序支付必传
	 */
	private String openId;

}
