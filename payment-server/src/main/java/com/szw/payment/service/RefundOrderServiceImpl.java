package com.szw.payment.service;

import java.time.LocalDateTime;

import com.szw.dubbo.plugin.common.result.ResponseCode;
import com.szw.dubbo.plugin.common.result.ServiceResponse;
import com.szw.payment.api.model.CompleteRefundForWxPayRequest;
import com.szw.payment.api.model.CreateRefundOrderRequest;
import com.szw.payment.api.model.QueryRefundOrderRequest;
import com.szw.payment.api.model.QueryRefundOrderResponse;
import com.szw.payment.api.service.RefundOrderService;
import com.szw.payment.common.WxPayKeys;
import com.szw.payment.common.model.Refund;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.entity.RefundOrder;
import com.szw.payment.exception.NotEnoughAmountException;
import com.szw.payment.facade.PayFacade;
import com.szw.payment.manager.ConfigManager;
import com.szw.payment.manager.PayOrderManager;
import com.szw.payment.manager.RefundOrderManager;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import com.wechat.pay.java.service.refund.model.Status;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import org.springframework.util.Assert;

@DubboService
@Slf4j(topic = "running")
public class RefundOrderServiceImpl implements RefundOrderService {

	@Inject
	private RefundOrderManager refundOrderManager;

	@Inject
	private PayOrderManager payOrderManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private PayFacade payFacade;


	@Override
	public ServiceResponse<Boolean> createRefundOrder(CreateRefundOrderRequest request) {
		String idempotentKey = request.getIdempotentKey();
		int count = refundOrderManager.countByIdempotentKey(idempotentKey);
		if (count == 0) {
			innerCreateRefundOrder(request);
		}
		return new ServiceResponse<>(ResponseCode.SUCCESS, "", true);
	}

	@Override
	public ServiceResponse<Boolean> completeRefundForWxPay(CompleteRefundForWxPayRequest request) {
		RefundNotification notification = validAndParseWxNotificationStr(request);
		Status refundStatus = notification.getRefundStatus();
		if (refundStatus == Status.PROCESSING) {
			// 理论上这种情况不存在
			return new ServiceResponse<>(ResponseCode.ERROR, "退款中，暂不处理");
		}

		LocalDateTime doneTime = null;
		String successTime = notification.getSuccessTime();
		if (StringUtils.isNotBlank(successTime)) {
			doneTime = LocalDateTime.parse(successTime, WxPayKeys.formatter);
		}

		boolean success = (refundStatus == Status.SUCCESS);
		String outRefundNo = notification.getOutRefundNo();
		refundOrderManager.completeRefund(outRefundNo, doneTime, success, null);
		return new ServiceResponse<>(ResponseCode.SUCCESS, "成功", true);
	}

	@Override
	public ServiceResponse<QueryRefundOrderResponse> queryRefundOrder(QueryRefundOrderRequest request) {
		String idempotentKey = request.getIdempotentKey();
		RefundOrder refundOrder = refundOrderManager.findByIdempotentKey(idempotentKey);
		QueryRefundOrderResponse response = Converter.buildQueryRefundOrderResponse(refundOrder);
		return new ServiceResponse<>(ResponseCode.SUCCESS, "成功", response);
	}

	private void innerCreateRefundOrder(CreateRefundOrderRequest request) {
		String tradeId = request.getTradeId();
		String channel = request.getChannel();
		PayOrder payOrder = payOrderManager.findByTradeIdAndChannel(tradeId, channel);
		Assert.notNull(payOrder, String.format("支付单不存在#tradeId:%s channel:%s", tradeId, channel));

		if (payOrder.getBalance() < request.getAmount()) {
			throw new NotEnoughAmountException(
					String.format("余额不足 支付单余额:%s 申请退款金额:%s",
							payOrder.getBalance(), request.getAmount()));
		}

		Refund refund = Converter.buildRefundFromCreateRefundOrderRequest(request);
		refundOrderManager.createRefundOrder(payOrder, refund);
	}

	private RefundNotification validAndParseWxNotificationStr(CompleteRefundForWxPayRequest request) {
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
		return parser.parse(requestParam, RefundNotification.class);
	}

}
