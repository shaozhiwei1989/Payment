package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CompleteRefundForWxPayRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -5264067466807152689L;

	private String appId;

	private String mchId;

	private String serialNumber;

	private String nonce;

	private String signature;

	private String signType;

	private String timestamp;

	private String body;

}
