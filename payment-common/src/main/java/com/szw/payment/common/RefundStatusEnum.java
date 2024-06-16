package com.szw.payment.common;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefundStatusEnum {
	INIT(0, "初始化"),

	WAITING_RETRY(1, "稍后重试"),

	REFUND_ING(2, "退款中"),

	REFUND_SUCCESS(3, "退款成功"),

	REFUND_ERR(4, "退款失败"),

	UNKNOWN(999, "未知");


	private final int code;

	private final String desc;


	public static RefundStatusEnum fromCode(Integer code) {
		return Arrays
				.stream(values())
				.filter(o -> Objects.equals(o.code, code))
				.findFirst()
				.orElse(UNKNOWN);
	}

}
