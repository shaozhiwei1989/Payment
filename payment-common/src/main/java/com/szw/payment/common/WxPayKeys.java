package com.szw.payment.common;

import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WxPayKeys {

	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

	public static final String REQUEST_ID = "Request-ID";

	public static final String NONCE = "Wechatpay-Nonce";

	public static final String SIGNATURE = "Wechatpay-Signature";

	public static final String TIMESTAMP = "Wechatpay-Timestamp";

	public static final String SERIAL = "Wechatpay-Serial";

	public static final String EVENT_TYPE = "event_type";

	public static final String PAY_SCORE_USER_PAID = "PAYSCORE.USER_PAID";

	public static final String RESULT_FAILURE = """
				{"code": "FAIL","message": "失败"}
			""";

	public static final String RESULT_SUCCESS = """
				{"code": "SUCCESS","message": "成功"}
			""";

}
