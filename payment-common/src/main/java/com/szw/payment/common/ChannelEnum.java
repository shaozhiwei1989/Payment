package com.szw.payment.common;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelEnum {
	WX_APP("wx_app", "1", "微信App支付"),

	WX_MINI("wx_mini", "2", "微信小程序支付"),

	ALIPAY("alipay", "3", "支付宝支付"),
	;

	private final String code;
	private final String identifier;
	private final String desc;


	public static ChannelEnum fromCode(String code) {
		return Arrays
				.stream(values())
				.filter(o -> Objects.equals(o.code, code))
				.findFirst()
				.orElseThrow();
	}

}
