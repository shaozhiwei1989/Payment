package com.szw.payment;

public interface ResultCode {
	int SUCCESS = 100000;
	int ERROR = 999999;

	int getCode();

	String getDesc();

}
