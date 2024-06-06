package com.szw.payment.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExtraKeys {

	public static final String NONCE_STR = "nonceStr";

	public static final String SIGN = "sign";

	public static final String SIGN_TYPE = "signType";

	public static final String TIME_STAMP = "timeStamp";

	public static final String PACKAGE_VALUE = "packageValue";

}
