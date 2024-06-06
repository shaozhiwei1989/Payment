package com.szw.payment.sdk.wxpay;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.response.PrepayResponse;
import com.szw.payment.common.response.QueryPayOrderResponse;
import com.szw.payment.sdk.Pay;
import com.wechat.pay.java.core.util.NonceUtil;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payments.model.Transaction;

public class WxMiniPay extends AbstractWxPay implements Pay {


	@Override
	public PrepayResponse prepay(Prepay prepay) {
		PrepayRequest request = new PrepayRequest();
		request.setAppid(configInfo.getAppId());
		request.setMchid(configInfo.getMchId());
		request.setNotifyUrl(configInfo.getNotifyUrl());
		request.setDescription(prepay.getBody());
		request.setOutTradeNo(prepay.getOutTradeNo());
		request.setAttach(prepay.getPassBackParams());

		Amount amount = new Amount();
		amount.setTotal(prepay.getTotalFee().intValue());
		request.setAmount(amount);

		Payer payer = new Payer();
		payer.setOpenid(prepay.getOpenId());
		request.setPayer(payer);

		JsapiService service = new JsapiService.Builder().config(wxConfig).build();
		com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse response = service.prepay(request);

		String nonceStr = NonceUtil.createNonce(32);
		String packageStr = "prepay_id=" + response.getPrepayId();
		String timeStamp = String.valueOf(System.currentTimeMillis() / 1000L);

		StringJoiner joiner = new StringJoiner("\n");
		joiner.add(configInfo.getAppId());
		joiner.add(timeStamp);
		joiner.add(nonceStr);
		joiner.add(packageStr);
		joiner.add("\n");

		String signStr = wxConfig.createSigner().sign(joiner.toString()).getSign();
		return PrepayResponse.builder()
				.prePayId(response.getPrepayId())
				.nonceStr(nonceStr)
				.timeStamp(timeStamp)
				.paySign(signStr)
				.packageStr(packageStr)
				.build();
	}


	@Override
	public QueryPayOrderResponse queryPayOrder(Prepay prepay) {
		QueryOrderByIdRequest request = new QueryOrderByIdRequest();
		request.setTransactionId(prepay.getTransactionId());

		JsapiService service = new JsapiService.Builder().config(wxConfig).build();
		Transaction transaction = service.queryOrderById(request);

		String tradeState = translatePayStatus(transaction.getTradeState().name());
		LocalDateTime successTime = convertStrToLocalDateTime(transaction.getSuccessTime());
		return QueryPayOrderResponse.builder()
				.outTradeNo(transaction.getOutTradeNo())
				.transactionId(transaction.getTransactionId())
				.tradeState(tradeState)
				.payDoneTime(successTime)
				.build();
	}

}
