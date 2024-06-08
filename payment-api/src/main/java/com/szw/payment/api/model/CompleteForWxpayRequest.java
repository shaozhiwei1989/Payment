package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CompleteForWxpayRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -4372203741247386216L;

	private String appId;

	private String mchId;

	private String serialNumber;

	private String nonce;

	private String signature;

	private String signType;

	private String timestamp;

	private String body;



//	/**
//	 * 事件类型
//	 * PAYSCORE.USER_PAID
//	 */
//	private String eventType;
//
//	/**
//	 * 算法
//	 * AEAD_AES_256_GCM
//	 */
//	private String algorithm;
//
//	/**
//	 *数据密文
//	 */
//	private String cipherText;
//
//	/**
//	 * 随机串
//	 */
//	private String nonce;
//
//	/**
//	 * 附加数据
//	 */
//	private String associatedData;

}
