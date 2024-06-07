package com.szw.payment;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
	@Serial
	private static final long serialVersionUID = -3726721471028770352L;

	private int code;

	private String desc;

	private T data;


	public static <T> Result<T> of(ResultCode code) {
		return new Result<>(code);
	}

	public Result(ResultCode code) {
		this.code = code.getCode();
		this.desc = code.getDesc();
	}

	public Result(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public boolean isSuccess() {
		return this.code == ResultCode.SUCCESS;
	}

}
