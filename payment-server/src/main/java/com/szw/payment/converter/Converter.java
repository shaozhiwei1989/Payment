package com.szw.payment.converter;

import java.time.LocalDateTime;

import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.common.ChannelEnum;
import com.szw.payment.common.Constants;
import com.szw.payment.common.ExtraKeys;
import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.PayOrderMessage;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
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


	public static PayOrder buildPayOrder(Config config, Prepay prepay, PrepayResponse response) {
		PayOrder payOrder = new PayOrder();
		payOrder.setConfigId(config.getId());
		payOrder.setChannel(config.getChannel());
		payOrder.setAmount(prepay.getTotalFee());
		payOrder.setOutTradeNo(prepay.getOutTradeNo());
		payOrder.setTradeId(prepay.getTradeId());
		payOrder.setUserId(prepay.getUserId());
		payOrder.setExpireTime(prepay.getExpireTime());
		payOrder.setBalance(prepay.getTotalFee());

		payOrder.setRefundAmount(0L);
		payOrder.setRefundFrozenAmount(0L);
		payOrder.setStatus(Constants.Pay.WAIT_PAY);

		payOrder.setPrePayId(response.getPrePayId());
		payOrder.putExtraParam(ExtraKeys.SIGN, response.getSign())
				.putExtraParam(ExtraKeys.SIGN_TYPE, response.getSignType())
				.putExtraParam(ExtraKeys.NONCE_STR, response.getNonceStr())
				.putExtraParam(ExtraKeys.TIME_STAMP, response.getTimeStamp())
				.putExtraParam(ExtraKeys.PACKAGE_VALUE, response.getPackageValue())
				.putExtraParam(ExtraKeys.APP_ID, config.getAppId())
				.putExtraParam(ExtraKeys.MCH_ID, config.getMchId())
				.generateExtraParam();

		return payOrder;
	}

	public static Prepay buildPrepay(Config config, PayOrderCreateRequest request) {
		Prepay prepay = new Prepay();
		prepay.setBody(request.getBody());
		prepay.setOpenId(request.getOpenId());
		prepay.setUserId(request.getUserId());
		prepay.setTradeId(request.getTradeId());
		prepay.setTotalFee(request.getTotalFee());

		ChannelEnum channelEnum = ChannelEnum.fromCode(config.getChannel());
		String outTradeNo = String.format("%s-%s", request.getTradeId(), channelEnum.getIdentifier());
		prepay.setOutTradeNo(outTradeNo); // 暂时由 订单id 和 支付渠道标识符 组成

		LocalDateTime expireTime = LocalDateTime.now().plusMinutes(90L);
		prepay.setExpireTime(expireTime);
		return prepay;
	}

	public static PayOrderResponse buildPayOrderResponse(PayOrder payOrder) {
		PayOrderResponse response = new PayOrderResponse();
		response.setTradeId(payOrder.getTradeId());
		response.setOutTradeNo(payOrder.getOutTradeNo());
		response.setPrePayId(payOrder.getPrePayId());

		response.setSign(payOrder.getExtraParamByKey(ExtraKeys.SIGN));
		response.setSignType(payOrder.getExtraParamByKey(ExtraKeys.SIGN_TYPE));
		response.setNonceStr(payOrder.getExtraParamByKey(ExtraKeys.NONCE_STR));
		response.setTimeStamp(payOrder.getExtraParamByKey(ExtraKeys.TIME_STAMP));
		response.setPackageValue(payOrder.getExtraParamByKey(ExtraKeys.PACKAGE_VALUE));
		response.setAppId(payOrder.getExtraParamByKey(ExtraKeys.APP_ID));
		response.setMchId(payOrder.getExtraParamByKey(ExtraKeys.MCH_ID));
		return response;
	}

	public static PayOrderMessage buildPayOrderMessage(PayOrder payOrder) {
		PayOrderMessage message = new PayOrderMessage();
		message.setPrePayId(payOrder.getPrePayId());
		message.setPayDoneTime(payOrder.getPayDoneTime());
		message.setChannel(payOrder.getChannel());
		message.setTransactionId(payOrder.getTransactionId());
		return message;
	}

}
