package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Valid
public class CreateRefundOrderRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -6825991801814132428L;

	/**
	 * 平台订单id
	 */
	@NotBlank(message = "订单id不能为空")
	private String tradeId;

	/**
	 * 平台用户id
	 */
	@NotBlank(message = "用户id不能为空")
	private String userId;

	/**
	 * 支付渠道
	 */
	@NotBlank(message = "支付渠道不能为空")
	private String channel;

	/**
	 * 幂等key 外部传入
	 * 要求全局唯一，建议使用业务系统标识 + 业务数据唯一键
	 */
	@NotBlank(message = "幂等key不能为空")
	private String idempotentKey;

	/**
	 * 支付流水号
	 */
	@NotBlank(message = "流水号不能为空")
	private String transactionId;

	/**
	 * 退款金额
	 */
	@NotNull(message = "退款金额不能为空")
	@Min(message = "退款金额必须大于0", value = 0)
	private Long amount;

	/**
	 * 退款描述
	 */
	private String description;

	/**
	 * 回传参数
	 */
	private Map<String, String> passBackParamMap = new HashMap<>();

}
