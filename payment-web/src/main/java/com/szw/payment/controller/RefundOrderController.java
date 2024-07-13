package com.szw.payment.controller;

import java.util.Objects;

import com.szw.dubbo.plugin.common.result.ServiceResponse;
import com.szw.payment.api.model.CompleteRefundForWxPayRequest;
import com.szw.payment.api.service.RefundOrderService;
import com.szw.payment.common.WxPayKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refund")
@Slf4j(topic = "running")
public class RefundOrderController {

	@DubboReference
	private RefundOrderService refundOrderService;


	@RequestMapping("/wxpay/notice/{appId}/{mchId}")
	public String wxpayNotice(@RequestBody String body,
			@PathVariable("appId") String appId,
			@PathVariable("mchId") String mchId,
			@RequestHeader(WxPayKeys.NONCE) String nonce,
			@RequestHeader(WxPayKeys.SIGNATURE) String signature,
			@RequestHeader(WxPayKeys.TIMESTAMP) String timeStamp,
			@RequestHeader(WxPayKeys.SERIAL) String serialNumber) {

		try {
			CompleteRefundForWxPayRequest request = new CompleteRefundForWxPayRequest();
			request.setAppId(appId);
			request.setMchId(mchId);
			request.setBody(body);
			request.setNonce(nonce);
			request.setSignature(signature);
			request.setTimestamp(timeStamp);
			request.setSerialNumber(serialNumber);
			ServiceResponse<Boolean> response = refundOrderService.completeRefundForWxPay(request);
			boolean isTrue = Objects.equals(response.getData(), true);
			return isTrue ? WxPayKeys.RESULT_SUCCESS : WxPayKeys.RESULT_FAILURE;
		}
		catch (Exception e) {
			log.error("wxpayNotice", e);
			return WxPayKeys.RESULT_FAILURE;
		}
	}
}
