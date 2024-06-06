package com.szw.payment.manager;

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
public class PayManager {

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

}
