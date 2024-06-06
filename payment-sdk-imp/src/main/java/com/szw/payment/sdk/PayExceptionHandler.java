package com.szw.payment.sdk;


import java.lang.reflect.Method;

import com.szw.payment.sdk.exception.PayException;

public interface PayExceptionHandler {

	/**
	 * 异常回调
	 * @param method 执行的方法
	 * @param exception 异常
	 * @return 返回true异常会重抛，false异常会被忽略
	 */
	boolean onException(Method method, PayException exception);

}
