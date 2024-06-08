package com.szw.payment.api.service;

import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CheckAliPaySignRequest;
import com.szw.payment.api.model.CompletePayRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;

public interface PayOrderService {

	ServiceResponse<PayOrderResponse> createPayOrder(PayOrderCreateRequest request);

	ServiceResponse<Boolean> completePay(CompletePayRequest request);

	ServiceResponse<Boolean> checkAliPaySign(CheckAliPaySignRequest request);
}
