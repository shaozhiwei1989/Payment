package com.szw.payment.converter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.szw.payment.api.model.CreateRefundOrderRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.common.ChannelEnum;
import com.szw.payment.common.Constants;
import com.szw.payment.common.ExtraKeys;
import com.szw.payment.common.RefundStatusEnum;
import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.PayOrderMessage;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundOrderMessage;
import com.szw.payment.common.utils.GsonUtil;
import com.szw.payment.common.utils.UUIDUtil;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.entity.RefundOrder;
import com.szw.payment.entity.RefundTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Converter {
	private static final Type TYPE_OF_MAP = new TypeToken<Map<String, String>>() {}.getType();


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
		message.setTradeId(payOrder.getTradeId());
		message.setUserId(payOrder.getUserId());
		return message;
	}

	public static RefundOrder buildRefundOrder(PayOrder payOrder, Refund refund) {
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setUserId(payOrder.getUserId());
		refundOrder.setTradeId(payOrder.getTradeId());
		refundOrder.setAmount(refund.getRefundFee());
		refundOrder.setIdempotentKey(refund.getIdempotentKey());
		refundOrder.setDescription(refund.getRefundDesc());
		refundOrder.setPassBackParam(refund.getPassBackParam());

		refundOrder.setPayOrderId(payOrder.getId());
		refundOrder.setConfigId(payOrder.getConfigId());

		refundOrder.setRetries(0);
		refundOrder.setOutRefundNo(UUIDUtil.uuid());
		refundOrder.setStatus(RefundStatusEnum.INIT.getCode());
		return refundOrder;
	}

	public static Refund buildRefundFromCreateRefundOrderRequest(CreateRefundOrderRequest request) {
		Refund refund = new Refund();
		refund.setRefundFee(request.getAmount());
		refund.setRefundDesc(request.getDescription());
		refund.setTransactionId(request.getTransactionId());
		refund.setIdempotentKey(request.getIdempotentKey());
		refund.setPassBackParam(GsonUtil.GSON.toJson(request.getPassBackParamMap()));
		return refund;
	}

	public static Refund buildRefund(PayOrder payOrder, RefundOrder refundOrder) {
		Refund refund = new Refund();
		refund.setTotalFee(payOrder.getAmount());
		refund.setOutTradeNo(payOrder.getOutTradeNo());
		refund.setTransactionId(payOrder.getTransactionId());

		refund.setRefundFee(refundOrder.getAmount());
		refund.setRefundDesc(refundOrder.getDescription());
		refund.setOutRefundNo(refundOrder.getOutRefundNo());
		return refund;
	}

	public static RefundOrderMessage buildRefundOrderMessage(RefundOrder refundOrder) {
		RefundOrderMessage message = new RefundOrderMessage();
		message.setAmount(refundOrder.getAmount());
		message.setStatus(refundOrder.getStatus());
		message.setIdempotentKey(refundOrder.getIdempotentKey());
		message.setUserId(refundOrder.getUserId());
		message.setTradeId(refundOrder.getTradeId());

		String passBackParam = refundOrder.getPassBackParam();
		Map<String, String> map = GsonUtil.GSON.fromJson(passBackParam, TYPE_OF_MAP);
		if (map != null && !map.isEmpty()) {
			message.getPassBackParamMap().putAll(map);
		}
		return message;
	}

	public static RefundTask buildRefundTask(RefundOrder refundOrder) {
		RefundTask refundTask = new RefundTask();
		refundTask.setRefundOrderId(refundOrder.getId());
		refundTask.setExecTime(LocalDateTime.now());
		refundTask.setUserId(refundOrder.getUserId());
		refundTask.setTradeId(refundOrder.getTradeId());
		return refundTask;
	}

}
