package com.szw.payment.facade;

import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundCreateResponse;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.common.model.PayOrderQueryResponse;
import com.szw.payment.common.model.RefundQueryResponse;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.sdk.Pay;
import com.szw.payment.sdk.PayBuilder;
import com.szw.payment.sdk.PayExceptionHandler;
import com.szw.payment.sdk.wxpay.AbstractWxPay;
import com.wechat.pay.java.core.notification.NotificationConfig;
import jakarta.inject.Named;

@Named
public class PayFacade {


	public PrepayResponse prepay(Config config, Prepay prepay) {
		ConfigInfo configInfo = Converter.buildConfigInfo(config);
		Pay pay = PayBuilder.create().config(configInfo).build();
		return pay.prepay(prepay);
	}

	public RefundCreateResponse createRefund(Config config, PayExceptionHandler handler, Refund refund) {
		ConfigInfo configInfo = Converter.buildConfigInfo(config);
		Pay pay = PayBuilder.create().config(configInfo).exceptionHandler(handler).build();
		return pay.createRefund(refund);
	}

	public PayOrderQueryResponse queryPayOrder(Config config, Prepay prepay) {
		ConfigInfo configInfo = Converter.buildConfigInfo(config);
		Pay pay = PayBuilder.create().config(configInfo).build();
		return pay.queryPayOrder(prepay);
	}

	public RefundQueryResponse queryRefundOrder(Config config, Refund refund) {
		ConfigInfo configInfo = Converter.buildConfigInfo(config);
		Pay pay = PayBuilder.create().config(configInfo).build();
		return pay.queryRefundOrder(refund);
	}

	public NotificationConfig getWxNotificationConfig(Config config) {
		ConfigInfo configInfo = Converter.buildConfigInfo(config);
		Pay pay = PayBuilder.create().config(configInfo).build();
		if (pay instanceof AbstractWxPay wxPay) {
			return (NotificationConfig) wxPay.getWxConfig();
		}
		throw new UnsupportedOperationException("支付类型错误 channel:" + config.getChannel());
	}

}
