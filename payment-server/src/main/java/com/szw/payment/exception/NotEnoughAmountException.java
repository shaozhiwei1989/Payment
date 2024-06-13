package com.szw.payment.exception;

import java.io.Serial;

public class NotEnoughAmountException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6970564397577614206L;


	public NotEnoughAmountException() {
	}

	public NotEnoughAmountException(String message) {
		super(message);
	}

	public NotEnoughAmountException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughAmountException(Throwable cause) {
		super(cause);
	}

	public NotEnoughAmountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
