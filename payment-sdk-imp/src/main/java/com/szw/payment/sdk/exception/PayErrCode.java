package com.szw.payment.sdk.exception;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayErrCode {

	CLIENT_API_ERR(10000001, "接口请求异常"),

	DATA_PARSER_ERR(10000002, "数据解析异常"),

	LOAD_CERT_ERR(10000003, "加载证书异常"),

	RETRY_LATER(10000004, "稍后重试"),

	NONE(10000099, "NONE");

	private final int code;

	private final String desc;


	public static PayErrCode fromCode(int code) {
		return Arrays
				.stream(values())
				.filter(o -> o.code == code)
				.findFirst()
				.orElse(NONE);
	}

}
