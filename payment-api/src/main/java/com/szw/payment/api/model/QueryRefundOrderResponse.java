package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class QueryRefundOrderResponse implements Serializable {
	@Serial
	private static final long serialVersionUID = -4256380117506823072L;

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
	 * 退款创建时间
	 */
	private LocalDateTime refundCreateTime;

	/**
	 * 退款完成时间
	 */
	private LocalDateTime refundEndTime;

	/**
	 * 退款失败描述
	 */
	private String refundFailDesc;

	/**
	 * 回传参数
	 */
	private final Map<String, String> passBackParamMap = new HashMap<>();

}
