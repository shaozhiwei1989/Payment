package com.szw.payment.api.service;

import com.szw.dubbo.plugin.common.result.ServiceResponse;
import com.szw.payment.api.model.CompleteForAlipayRequest;
import com.szw.payment.api.model.CompleteForWxpayRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;

public interface PayOrderService {

	ServiceResponse<PayOrderResponse> createPayOrder(PayOrderCreateRequest request);

	ServiceResponse<Boolean> completeForAlipay(CompleteForAlipayRequest request);

	ServiceResponse<Boolean> completeForWxPay(CompleteForWxpayRequest request);

}
