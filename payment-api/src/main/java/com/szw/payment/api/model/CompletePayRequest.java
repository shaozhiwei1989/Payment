package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompletePayRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = 3123193503089663282L;

	private String outTradeNo;

	private String transactionId;

	private LocalDateTime payTime;

}
