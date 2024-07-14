package com.szw.payment.common.model;

import com.szw.payment.common.ExtraKeys;
import com.szw.payment.common.annotation.ExtraKey;
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
	@ExtraKey(value = ExtraKeys.NONCE_STR)
	private String nonceStr;

	/**
	 * 支付签名
	 */
	@ExtraKey(value = ExtraKeys.SIGN)
	private String sign;

	/**
	 * 签名类型
	 */
	@ExtraKey(value = ExtraKeys.SIGN_TYPE)
	private String signType;

	/**
	 * 时间戳 (仅微信相关支付)
	 */
	@ExtraKey(value = ExtraKeys.TIME_STAMP)
	private String timeStamp;

	/**
	 * 微信支付签名串 (仅微信相关支付)
	 */
	@ExtraKey(value = ExtraKeys.PACKAGE_VALUE)
	private String packageValue;

	/**
	 * 微信支付：商户号绑定的appID
	 * 支付宝支付：支付宝商家应用ID
	 */
	@ExtraKey(value = ExtraKeys.APP_ID)
	private String appId;

	/**
	 * 支付商户号 微信支付专用
	 */
	@ExtraKey(value = ExtraKeys.MCH_ID)
	private String mchId;

}
