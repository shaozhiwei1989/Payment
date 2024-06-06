package com.szw.payment.common.response;

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
	 * 签名方式 (仅微信相关支付)
	 */
	private String signType;

	/**
	 * 支付签名
	 */
	private String paySign;

	/**
	 * 时间戳 (仅微信相关支付)
	 */
	private String timeStamp;

	/**
	 * 微信支付签名串 (仅微信相关支付)
	 */
	private String packageStr;

}
