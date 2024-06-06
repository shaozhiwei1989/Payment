package com.szw.payment.manager;

import java.time.LocalDateTime;

import com.szw.payment.common.Constants;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.response.PrepayResponse;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.facade.PayFacade;
import com.szw.payment.store.PayOrderStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class PayOrderManager {

	@Inject
	private PayFacade payFacade;

	@Inject
	private PayOrderStore payOrderStore;


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

		if (!Constants.Pay.NOT_PAY.equals(payOrder.getStatus())) {
			throw new RuntimeException(
					String.format("支付单不是NOT_PAY状态# outTradeNo:%s  status:%s",
							outTradeNo, payOrder.getStatus()));
		}

		int rows = payOrderStore.completePay(payOrder.getId(),
				Constants.Pay.NOT_PAY, Constants.Pay.SUCCESS, transactionId, payTime);
		return rows > 0;
	}

}
