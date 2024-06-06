package com.szw.payment.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@Table("config")
public class Config implements Serializable {

	/**
	 * id
	 */
	@Id
	@Column("id")
	private Long id;

	/**
	 * 支付渠道
	 */
	@Column("channel")
	private String channel;

	/**
	 * 微信支付：商户号绑定的appID
	 * 支付宝支付：支付宝商家应用ID
	 */
	@Column("app_id")
	private String appId;

	/**
	 * 支付商户号 微信支付专用
	 */
	@Column("mch_id")
	private String mchId;

	/**
	 * 商户号证书序列号 微信支付专用
	 */
	@Column("mch_serial_number")
	private String mchSerialNumber;

	/**
	 * api密钥
	 */
	@Column("api_key")
	private String apiKey;

	/**
	 * 公钥
	 */
	@Column("public_key")
	private String publicKey;

	/**
	 * 私钥
	 */
	@Column("private_key")
	private String privateKey;

	/**
	 * 支付回调地址
	 */
	@Column("notify_url")
	private String notifyUrl;

	/**
	 * 退款回调地址
	 */
	@Column("refund_url")
	private String refundUrl;

	/**
	 * api版本
	 */
	@Column("api_version")
	private String apiVersion;

	/**
	 * 描述
	 */
	@Column("description")
	private String description;

	/**
	 * 是否删除
	 */
	@Column("is_deleted")
	private boolean deleted;

}
