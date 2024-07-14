package com.szw.payment.sdk.mockpay;

import java.time.LocalDateTime;

import com.szw.payment.common.Constants;
import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.PayOrderQueryResponse;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundCreateResponse;
import com.szw.payment.common.model.RefundQueryResponse;
import com.szw.payment.common.utils.UUIDUtil;
import com.szw.payment.sdk.Pay;

public class MockPay implements Pay {

	private ConfigInfo configInfo;


	@Override
	public void initConfig(ConfigInfo configInfo) throws Exception {
		this.configInfo = configInfo;
	}

	@Override
	public PrepayResponse prepay(Prepay prepay) {
		String prePayId = "mockpay@" + UUIDUtil.uuid();
		return PrepayResponse.builder()
				.prePayId(prePayId)
				.sign("mockpay" + System.currentTimeMillis())
				.appId(configInfo.getAppId())
				.mchId(configInfo.getMchId())
				.build();
	}

	@Override
	public RefundCreateResponse createRefund(Refund refund) {
		return RefundCreateResponse.builder()
				.transactionId(refund.getTransactionId())
				.outRefundNo(refund.getOutRefundNo())
				.waitCallBack(true)
				.build();
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

}
