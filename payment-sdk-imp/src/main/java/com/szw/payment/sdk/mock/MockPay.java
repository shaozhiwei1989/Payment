package com.szw.payment.sdk.mock;

import java.time.LocalDateTime;
import java.util.UUID;

import com.szw.payment.common.Constants;
import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.PayOrderQueryResponse;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundCreateResponse;
import com.szw.payment.common.model.RefundQueryResponse;
import com.szw.payment.sdk.Pay;
import com.szw.payment.sdk.exception.PayErrCode;
import com.szw.payment.sdk.exception.PayException;

public class MockPay implements Pay {

	@Override
	public void initConfig(ConfigInfo configInfo) throws Exception {

	}

	@Override
	public PrepayResponse prepay(Prepay prepay) {
		return PrepayResponse.builder()
				.prePayId(genPrePayId())
				.sign("mockpay" + System.currentTimeMillis())
				.build();
	}

	@Override
	public RefundCreateResponse createRefund(Refund refund) {
		throw new PayException(PayErrCode.RETRY_LATER, "aaaa");
//		return RefundCreateResponse.builder()
//				.transactionId(refund.getTransactionId())
//				.outRefundNo(refund.getOutRefundNo())
//				.waitCallBack(true)
//				.build();
	}

	@Override
	public PayOrderQueryResponse queryPayOrder(Prepay prepay) {
		return PayOrderQueryResponse.builder()
				.tradeState(Constants.Pay.SUCCESS)
				.outTradeNo(prepay.getOutTradeNo())
				.transactionId(System.currentTimeMillis() + "")
				.payDoneTime(LocalDateTime.now())
				.build();
	}

	@Override
	public RefundQueryResponse queryRefundOrder(Refund refund) {
		return RefundQueryResponse.builder()
				.outTradeNo(refund.getOutTradeNo())
				.refundStatus(Constants.Refund.SUCCESS)
				.build();
	}

	private static String genPrePayId() {
		return "mockpay" + UUID.randomUUID().toString().replace("-", "");
	}
}
