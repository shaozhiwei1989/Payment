package com.szw.payment.sdk;


import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundCreateResponse;
import com.szw.payment.common.model.PrepayResponse;
import com.szw.payment.common.model.PayOrderQueryResponse;
import com.szw.payment.common.model.RefundQueryResponse;


public interface Pay {

	/**
	 * 初始化配置
	 */
	void initConfig(ConfigInfo configInfo) throws Exception;

	/**
	 * 预支付
	 */
	PrepayResponse prepay(Prepay prepay);

	/**
	 * 创建退款
	 */
	RefundCreateResponse createRefund(Refund refund);

	/**
	 * 查询支付订单
	 */
	PayOrderQueryResponse queryPayOrder(Prepay prepay);

	/**
	 * 查询退款订单
	 */
	RefundQueryResponse queryRefundOrder(Refund refund);

}
