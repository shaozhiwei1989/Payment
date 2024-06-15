package com.szw.payment.api;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceResponse<T> implements Serializable {
	@Serial
	private static final long serialVersionUID = -3726721471028770352L;

	private int code;

	private String desc;

	private T data;


	public static <T> ServiceResponse<T> of(ResponseCode code) {
		return new ServiceResponse<>(code);
	}

	public ServiceResponse(ResponseCode code) {
		this.code = code.getCode();
		this.desc = code.getDesc();
	}

	public ServiceResponse(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public boolean isSuccess() {
		return this.code == ResponseCode.SUCCESS;
	}

}
