package com.szw.payment.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ConfigInfo {

	/**
	 * 支付渠道
	 */
	private String channel;

	/**
	 * 微信支付：商户号绑定的appID
	 * 支付宝支付：支付宝商家应用ID
	 */
	private String appId;

	/**
	 * 支付商户号 微信支付专用
	 */
	private String mchId;

	/**
	 * 商户号证书序列号 微信支付专用
	 */
	private String mchSerialNumber;

	/**
	 * api密钥
	 */
	private String apiKey;

	/**
	 * 公钥
	 */
	private String publicKey;

	/**
	 * 私钥
	 */
	private String privateKey;

	/**
	 * 支付回调地址
	 */
	private String notifyUrl;

	/**
	 * 退款回调地址
	 */
	private String refundUrl;

	/**
	 * api版本
	 */
	private String apiVersion;

}
