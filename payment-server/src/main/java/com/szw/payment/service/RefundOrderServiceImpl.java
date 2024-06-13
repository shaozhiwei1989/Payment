package com.szw.payment.service;

import com.szw.payment.api.ResponseCode;
import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CreateRefundOrderRequest;
import com.szw.payment.api.service.RefundOrderService;
import com.szw.payment.common.model.Refund;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.exception.NotEnoughAmountException;
import com.szw.payment.manager.PayOrderManager;
import com.szw.payment.manager.RefundOrderManager;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import org.springframework.util.Assert;

@DubboService
@Slf4j(topic = "running")
public class RefundOrderServiceImpl implements RefundOrderService {

	@Inject
	private RefundOrderManager refundOrderManager;

	@Inject
	private PayOrderManager payOrderManager;


	@Override
	public ServiceResponse<Boolean> createRefundOrder(CreateRefundOrderRequest request) {
		try {
			String idempotentKey = request.getIdempotentKey();
			int count = refundOrderManager.countByIdempotentKey(idempotentKey);
			if (count == 0) {
				innerCreateRefundOrder(request);
			}
			return new ServiceResponse<>(ResponseCode.SUCCESS, "", true);
		}
		catch (Exception e) {
			log.error("", e);
			return new ServiceResponse<>(ResponseCode.ERROR, "接口异常");
		}
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

}
