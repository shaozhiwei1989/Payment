package com.szw.payment.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PrepayResponse {

	/**
	 * 支付id
	 */
	private String prePayId;

	/**
	 * 签名字符串 (仅微信相关支付)
	 */
	private String nonceStr;

	/**
	 * 支付签名
	 */
	private String sign;

	/**
	 * 签名类型
	 */
	private String signType;

	/**
	 * 时间戳 (仅微信相关支付)
	 */
	private String timeStamp;

	/**
	 * 微信支付签名串 (仅微信相关支付)
	 */
	private String packageValue;

}
