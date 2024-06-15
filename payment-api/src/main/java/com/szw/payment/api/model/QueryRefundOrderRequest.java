package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class QueryRefundOrderRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -223594064242182027L;

	/**
	 * 幂等key 外部传入
	 * 要求全局唯一，建议使用业务系统标识 + 业务数据唯一键
	 */
	private String idempotentKey;

}
