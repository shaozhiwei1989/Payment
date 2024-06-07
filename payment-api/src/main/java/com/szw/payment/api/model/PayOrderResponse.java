package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayOrderResponse implements Serializable {
	@Serial
	private static final long serialVersionUID = 5422845525813016049L;

	private String appId;

	private String mchId;

	private String nonceStr;

	private String sign;

	private String signType;

	private String timeStamp;

	private String packageValue;

	private String prePayId;

	private String tradeId;

	private String outTradeNo;

}
