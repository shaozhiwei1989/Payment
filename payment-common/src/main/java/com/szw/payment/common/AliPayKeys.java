package com.szw.payment.common;

import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AliPayKeys {
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

	public static final String FORMAT = "json";

	public static final String CHARSET = "UTF-8";

	public static final String SIGN_TYPE = "RSA2";

	public static final String PRODUCT_CODE = "QUICK_MSECURITY_PAY";

	public static final String OUT_TRADE_NO = "out_trade_no";

	public static final String TRADE_NO = "trade_no";

	public static final String TOTAL_AMOUNT = "total_amount";

	public static final String SELLER_ID = "seller_id";

	public static final String APP_ID = "app_id";

	public static final String GMT_PAYMENT = "gmt_payment";

	public static final String TRADE_STATUS = "trade_status";

	public static final String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";

	public static final String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

	public static final String TRADE_STATUS_TRADE_CLOSED = "TRADE_CLOSED";

	public static final String TRADE_STATUS_TRADE_FINISHED = "TRADE_FINISHED";

	public static final String RESULT_FAILURE = "failure";

	public static final String RESULT_SUCCESS = "success";

}
