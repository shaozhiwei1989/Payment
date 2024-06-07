package com.szw.payment.service;

import java.time.LocalDateTime;
import java.util.Objects;

import com.szw.payment.api.ResponseCode;
import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CompletePayRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.api.service.PayOrderService;
import com.szw.payment.common.Constants;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.manager.ConfigManager;
import com.szw.payment.manager.PayOrderManager;
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
	public ServiceResponse<Boolean> completePay(CompletePayRequest request) {
		try {
			String outTradeNo = request.getOutTradeNo();
			LocalDateTime payTime = request.getPayTime();
			String transactionId = request.getTransactionId();

			boolean b = payOrderManager.completePay(outTradeNo, transactionId, payTime);
			return new ServiceResponse<>(ResponseCode.SUCCESS, "成功", b);
		}
		catch (Exception e) {
			log.error("completePay", e);
			return new ServiceResponse<>(ResponseCode.ERROR, "接口异常");
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

}