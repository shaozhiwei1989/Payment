package com.szw.payment.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import com.alipay.api.internal.util.AlipaySignature;
import com.szw.payment.api.ResponseCode;
import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CompleteForAlipayRequest;
import com.szw.payment.api.model.CompleteForWxpayRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.api.service.PayOrderService;
import com.szw.payment.common.AliPayKeys;
import com.szw.payment.common.Constants;
import com.szw.payment.common.WxPayKeys;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.utils.GsonUtil;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.facade.PayFacade;
import com.szw.payment.manager.ConfigManager;
import com.szw.payment.manager.PayOrderManager;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@Slf4j(topic = "running")
public class PayOrderServiceImpl implements PayOrderService {

	@Inject
	private PayOrderManager payOrderManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private PayFacade payFacade;


	@Override
	public ServiceResponse<PayOrderResponse> createPayOrder(PayOrderCreateRequest request) {
		try {
			String tradeId = request.getTradeId();
			String channel = request.getChannel();

			PayOrder payOrder = payOrderManager.findByTradeIdAndChannel(tradeId, channel);
			if (!checkIsValidIfNotNull(payOrder)) {
				return new ServiceResponse<>(ResponseCode.ERROR, "支付单已经失效");
			}
			if (payOrder == null) {
				Config config = configManager.findOneByChannel(channel);
				Prepay prepay = Converter.buildPrepay(config, request);
				payOrder = payOrderManager.createPayOrder(config, prepay);
			}
			PayOrderResponse response = Converter.buildPayOrderResponse(payOrder);
			return new ServiceResponse<>(ResponseCode.SUCCESS, "成功", response);
		}
		catch (Exception e) {
			log.error("createPayOrder", e);
			return new ServiceResponse<>(ResponseCode.ERROR, "接口异常");
		}
	}

	@Override
	public ServiceResponse<Boolean> completeForAlipay(CompleteForAlipayRequest request) {
		try {
			Map<String, String> paramsMap = request.getParamsMap();
			String appId = paramsMap.get(AliPayKeys.APP_ID);

			Config config = configManager.findOneByAppId(appId);
			boolean checked = AlipaySignature.rsaCheckV1(paramsMap, config.getPublicKey(),
					AliPayKeys.CHARSET, AliPayKeys.SIGN_TYPE);

			if (!checked) {
				log.info("验证签名失败: map {}", paramsMap);
				return new ServiceResponse<>(ResponseCode.ERROR, "验证签名失败", false);
			}

			String tradeStatus = paramsMap.get(AliPayKeys.TRADE_STATUS);
			if (!AliPayKeys.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
				log.info("非支付成功状态: map {}", paramsMap);
				// 忽略 非支付成功状态 的回调，返回成功 避免请求重推
				return new ServiceResponse<>(ResponseCode.ERROR, "非支付成功状态", true);
			}

			String outTradeNo = paramsMap.get(AliPayKeys.OUT_TRADE_NO);
			String transactionId = paramsMap.get(AliPayKeys.TRADE_NO);
			String gmtPayment = paramsMap.get(AliPayKeys.GMT_PAYMENT);
			LocalDateTime payTime = LocalDateTime.parse(gmtPayment, AliPayKeys.formatter);

			boolean ok = payOrderManager.completePay(outTradeNo, transactionId, payTime);
			return new ServiceResponse<>(ResponseCode.SUCCESS, "成功", ok);
		}
		catch (Exception e) {
			log.error("completeForAlipay", e);
			return new ServiceResponse<>(ResponseCode.ERROR, "接口异常");
		}
	}

	@Override
	public ServiceResponse<Boolean> completeForWxPay(CompleteForWxpayRequest request) {
		try {
			Transaction transaction = validAndParseWxNotificationStr(request);

			Transaction.TradeStateEnum tradeState = transaction.getTradeState();
			if (tradeState != Transaction.TradeStateEnum.SUCCESS) {
				log.info("非支付成功状态:transaction:{}", GsonUtil.GSON.toJson(transaction));
				// 忽略 非支付成功状态 的回调，返回成功 避免请求重推
				return new ServiceResponse<>(ResponseCode.SUCCESS, "非支付成功状态", true);
			}

			String outTradeNo = transaction.getOutTradeNo();
			String transactionId = transaction.getTransactionId();
			String successTime = transaction.getSuccessTime();
			LocalDateTime payTime = LocalDateTime.parse(successTime, WxPayKeys.formatter);

			boolean ok = payOrderManager.completePay(outTradeNo, transactionId, payTime);
			return new ServiceResponse<>(ResponseCode.SUCCESS, "成功", ok);
		}
		catch (Exception e) {
			log.error("", e);
			String msg = (e instanceof ValidationException) ? "签名验证失败" : "接口异常";
			return new ServiceResponse<>(ResponseCode.ERROR, msg);
		}
	}

	private static boolean checkIsValidIfNotNull(PayOrder payOrder) {
		if (payOrder != null) {
			if (!Objects.equals(Constants.Pay.WAIT_PAY, payOrder.getStatus())) {
				return false;
			}
			LocalDateTime expireTime = payOrder.getExpireTime();
			return expireTime.isAfter(LocalDateTime.now());
		}
		return true;
	}

	private Transaction validAndParseWxNotificationStr(CompleteForWxpayRequest request) {
		Config config = configManager.findByAppIdAndMchId(request.getAppId(), request.getMchId());

		NotificationConfig rsaConfig = payFacade.getWxNotificationConfig(config);
		NotificationParser parser = new NotificationParser(rsaConfig);
		RequestParam requestParam = new RequestParam.Builder()
				.serialNumber(request.getSerialNumber())
				.nonce(request.getNonce())
				.signature(request.getSignature())
				.signType(request.getSignType())
				.timestamp(request.getTimestamp())
				.body(request.getBody())
				.build();
		return parser.parse(requestParam, Transaction.class);
	}

}
