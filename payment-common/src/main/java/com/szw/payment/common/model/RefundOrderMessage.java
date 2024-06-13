package com.szw.payment.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RefundOrderMessage implements Serializable {
	@Serial
	private static final long serialVersionUID = -8551509765891219927L;

	/**
	 * 平台订单id
	 */
	private String tradeId;

	/**
	 * 平台用户id
	 */
	private String userId;

	/**
	 * 幂等key 外部传入
	 */
	private String idempotentKey;

	/**
	 * 退款金额
	 */
	private Long amount;

	/**
	 * 退款状态
	 */
	private int status;

	/**
	 * 回传参数
	 */
	private final Map<String, String> passBackParamMap = new HashMap<>();

}
