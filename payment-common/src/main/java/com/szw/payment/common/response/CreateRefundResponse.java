package com.szw.payment.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateRefundResponse {

	/**
	 * 平台退款单号
	 */
	private String outRefundNo;

	/**
	 * 支付流水号
	 */
	private String transactionId;

	/**
	 * 是否等待回调
	 */
	@Builder.Default
	private boolean waitCallBack = true;

}
