package com.szw.payment.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CompleteForAlipayRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -8550798815937423472L;

	private Map<String, String> paramsMap;

}
