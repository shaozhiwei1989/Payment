package com.szw.payment.sdk.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class PayException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4996667183858921589L;

    private final PayErrCode errCode;


    public PayException(PayErrCode errCode) {
        this.errCode = errCode;
    }

    public PayException(PayErrCode errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public PayException(PayErrCode errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public PayException(PayErrCode errCode, Throwable cause) {
        super(cause);
        this.errCode = errCode;
    }

    public PayException(PayErrCode errCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
    }

    public boolean isRetryLater() {
        return this.errCode == PayErrCode.RETRY_LATER;
    }


}
