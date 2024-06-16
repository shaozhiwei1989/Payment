package com.szw.payment.common.utils;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UUIDUtil {

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
