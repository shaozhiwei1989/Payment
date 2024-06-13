package com.szw.payment.exception;

import java.io.Serial;

public class UpdateDataException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -3004302534471156163L;

	public UpdateDataException() {
	}

	public UpdateDataException(String message) {
		super(message);
	}

	public UpdateDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateDataException(Throwable cause) {
		super(cause);
	}

	public UpdateDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
