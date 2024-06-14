package com.szw.payment.api.service;

import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CompleteRefundForWxPayRequest;
import com.szw.payment.api.model.CreateRefundOrderRequest;

public interface RefundOrderService {

	ServiceResponse<Boolean> createRefundOrder(CreateRefundOrderRequest request);

	ServiceResponse<Boolean> completeRefundForWxPay(CompleteRefundForWxPayRequest request);

}
