package com.szw.payment.bean;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PrepayBean implements Serializable {
	@Serial
	private static final long serialVersionUID = 7984605955545012906L;

	/**
	 * 支付渠道
	 */
	private String channel;

	/**
	 * 平台订单id
	 */
	private String tradeId;

	/**
	 * 平台用户id
	 */
	private String userId;

}
