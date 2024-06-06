package com.szw.payment.converter;

import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.entity.Config;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Converter {


	public static ConfigInfo buildConfigInfo(Config config) {
		ConfigInfo configInfo = new ConfigInfo();
		configInfo.setChannel(config.getChannel());
		configInfo.setApiKey(config.getApiKey());
		configInfo.setApiVersion(config.getApiVersion());
		configInfo.setNotifyUrl(config.getNotifyUrl());
		configInfo.setRefundUrl(config.getRefundUrl());
		configInfo.setAppId(config.getAppId());
		configInfo.setMchId(config.getMchId());
		configInfo.setMchSerialNumber(config.getMchSerialNumber());
		configInfo.setPublicKey(config.getPublicKey());
		configInfo.setPrivateKey(config.getPrivateKey());
		return configInfo;
	}

}
