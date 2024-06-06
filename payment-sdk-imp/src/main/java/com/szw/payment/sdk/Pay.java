package com.szw.payment.sdk;


import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.Prepay;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.response.CreateRefundResponse;
import com.szw.payment.common.response.PrepayResponse;
import com.szw.payment.common.response.QueryPayOrderResponse;
import com.szw.payment.common.response.QueryRefundOrderResponse;


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
	CreateRefundResponse createRefund(Refund refund);

	/**
	 * 查询支付订单
	 */
	QueryPayOrderResponse queryPayOrder(Prepay prepay);

	/**
	 * 查询退款订单
	 */
	QueryRefundOrderResponse queryRefundOrder(Refund refund);

}
