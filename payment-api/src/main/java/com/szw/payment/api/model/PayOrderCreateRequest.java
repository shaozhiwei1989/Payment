package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
	@NotBlank(message = "支付渠道不能为空")
	private String channel;

	/**
	 * 商品描述
	 */
	@NotBlank(message = "商品描述不能为空")
	private String body;

	/**
	 * 支付金额
	 */
	@NotNull(message = "支付金额不能为空")
	private Long totalFee;

	/**
	 * 平台订单id
	 */
	@NotBlank(message = "订单id不能为空")
	private String tradeId;

	/**
	 * 平台用户id
	 */
	@NotBlank(message = "用户id不能为空")
	private String userId;

	/**
	 * 用户标识 微信小程序支付必传
	 */
	private String openId;

}
