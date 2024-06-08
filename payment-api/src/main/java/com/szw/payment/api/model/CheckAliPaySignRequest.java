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
public class CheckAliPaySignRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = -3137506478615018204L;

	private String appId;

	private Map<String, String> checkParamsMap;

}
