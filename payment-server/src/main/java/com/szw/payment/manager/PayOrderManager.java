package com.szw.payment.manager;

import java.time.LocalDateTime;

import com.szw.payment.common.Constants;
import com.szw.payment.common.model.PayOrderMessage;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.facade.PayFacade;
import com.szw.payment.producer.MessageProducer;
import com.szw.payment.store.PayOrderStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.springframework.beans.factory.annotation.Value;

@Named
public class PayOrderManager {

	@Inject
	private PayFacade payFacade;

	@Inject
	private PayOrderStore payOrderStore;

	@Value("#{${message-producer.name}}")
	private MessageProducer<PayOrderMessage> messageProducer;

	@Value("${mq-topic.pay}")
	private String topic;


	public PayOrder createPayOrder(Config config, Prepay prepay) {
		PrepayResponse response = payFacade.prepay(config, prepay);

		PayOrder payOrder = Converter.buildPayOrder(config, prepay, response);
		payOrderStore.save(payOrder);
		return payOrder;
	}

	public boolean completePay(String outTradeNo, String transactionId, LocalDateTime payTime) {
		PayOrder payOrder = payOrderStore.findByOutTradeNo(outTradeNo);
		if (payOrder == null) {
			throw new RuntimeException("outTradeNo不存在#" + outTradeNo);
		}
		if (Constants.Pay.SUCCESS.equals(payOrder.getStatus())) {
			return true;
		}
		if (!Constants.Pay.WAIT_PAY.equals(payOrder.getStatus())) {
			throw new RuntimeException(
					String.format("支付单不是未付款状态# outTradeNo:%s  status:%s",
							outTradeNo, payOrder.getStatus()));
		}

		payOrder.setPayDoneTime(payTime);
		payOrder.setTransactionId(transactionId);
		payOrder.setStatus(Constants.Pay.SUCCESS);
		int rows = payOrderStore.completePay(payOrder, Constants.Pay.WAIT_PAY);

		boolean updated = (rows > 0);
		if (updated) {
			PayOrderMessage message = Converter.buildPayOrderMessage(payOrder);
			messageProducer.send(topic, Constants.Tag.SUCCESS, message);
		}
		return updated;
	}

	public PayOrder findByTradeIdAndChannel(String tradeId, String channel) {
		return payOrderStore.findByTradeIdAndChannel(tradeId, channel);
	}

}
