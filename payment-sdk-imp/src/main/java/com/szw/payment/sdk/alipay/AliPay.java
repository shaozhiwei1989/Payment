package com.szw.payment.sdk.alipay;

import static com.szw.payment.common.AliPayKeys.TRADE_STATUS_SUCCESS;
import static com.szw.payment.common.AliPayKeys.TRADE_STATUS_TRADE_CLOSED;
import static com.szw.payment.common.AliPayKeys.TRADE_STATUS_TRADE_FINISHED;
import static com.szw.payment.common.AliPayKeys.TRADE_STATUS_WAIT_BUYER_PAY;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.szw.payment.common.AliPayKeys;
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
import org.apache.commons.lang3.StringUtils;

public class AliPay implements Pay {

	private static final Set<String> retryCode = Set.of(
			"acq.system_error", "aop.acq.system_error",
			"aop.unknow-error", "isp.unknow-error", "isv.invalid-app-id",
			"acq.refund_charge_error", "aop.acq.refund_charge_error",
			"acq.seller_balance_not_enough", "aop.acq.seller_balance_not_enough");

	private static final Map<String /* appId */, AlipayClient> clientCache = new ConcurrentHashMap<>();


	private ConfigInfo configInfo;

	private AlipayClient client;


	@Override
	public void initConfig(ConfigInfo configInfo) throws Exception {
		this.configInfo = configInfo;
		this.client = clientCache.computeIfAbsent(configInfo.getAppId(),
				appId -> new DefaultAlipayClient(
						AliPayKeys.SERVER_URL,
						configInfo.getAppId(),
						configInfo.getPrivateKey(),
						AliPayKeys.FORMAT,
						AliPayKeys.CHARSET,
						configInfo.getPublicKey(),
						AliPayKeys.SIGN_TYPE));
	}

	@Override
	public PrepayResponse prepay(Prepay prepay) {
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setTotalAmount(String.valueOf(prepay.getTotalFee() / 100F));
		model.setPassbackParams(prepay.getPassBackParams());
		model.setOutTradeNo(prepay.getOutTradeNo());
		model.setProductCode(AliPayKeys.PRODUCT_CODE);
		model.setSubject(prepay.getBody());
		model.setBody(prepay.getBody());

		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setNotifyUrl(configInfo.getNotifyUrl());
		request.setBizModel(model);

		AlipayTradeAppPayResponse response;
		try {
			response = client.sdkExecute(request);
		}
		catch (AlipayApiException e) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, "Alipay创建订单失败, AlipayApiException,"
					+ " #errCode: " + e.getErrCode() + " #errMsg: " + e.getErrMsg());
		}

		if (!response.isSuccess()) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, errMsg("Alipay创建订单失败", response));
		}

		return PrepayResponse.builder()
				.prePayId(genPrePayId())
				.sign(response.getBody())
				.build();
	}

	@Override
	public RefundCreateResponse createRefund(Refund refund) {
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		model.setRefundAmount(String.valueOf(refund.getTotalFee() / 100F));
		model.setTradeNo(refund.getTransactionId());
		model.setOutRequestNo(refund.getOutRefundNo());

		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setNotifyUrl(configInfo.getNotifyUrl());
		request.setBizModel(model);

		AlipayTradeRefundResponse response;
		try {
			response = client.execute(request);
		}
		catch (AlipayApiException e) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, "Alipay退款接口失败, AlipayApiException,"
					+ " #errCode: " + e.getErrCode() + " #errMsg: " + e.getErrMsg());
		}

		if (!response.isSuccess()) {
			String errMsg = errMsg("Alipay退款接口失败", response);
			if (retryCode.contains(StringUtils.lowerCase(response.getSubCode()))) {
				throw new PayException(PayErrCode.RETRY_LATER, errMsg);
			}
			throw new PayException(PayErrCode.CLIENT_API_ERR, errMsg);
		}

		return RefundCreateResponse.builder()
				.transactionId(response.getTradeNo())
				.outRefundNo(refund.getOutRefundNo())
				.waitCallBack(false)
				.build();
	}

	@Override
	public PayOrderQueryResponse queryPayOrder(Prepay prepay) {
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setTradeNo(prepay.getTransactionId());

		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setNotifyUrl(configInfo.getNotifyUrl());
		request.setBizModel(model);

		AlipayTradeQueryResponse response;
		try {
			response = client.execute(request);
		}
		catch (AlipayApiException e) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, "Alipay交易查询接口失败, AlipayApiException,"
					+ " #errCode: " + e.getErrCode() + " #errMsg: " + e.getErrMsg());
		}

		if (!response.isSuccess()) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, errMsg("Alipay交易查询接口失败", response));
		}

		LocalDateTime payDoneTime = null;
		if (response.getSendPayDate() != null) {
			payDoneTime = LocalDateTime.from(response.getSendPayDate().toInstant());
		}

		return PayOrderQueryResponse.builder()
				.tradeState(translatePayStatus(response.getTradeStatus()))
				.outTradeNo(response.getOutTradeNo())
				.transactionId(response.getTradeNo())
				.payDoneTime(payDoneTime)
				.build();
	}

	@Override
	public RefundQueryResponse queryRefundOrder(Refund refund) {
		AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
		model.setTradeNo(refund.getTransactionId());
		model.setOutRequestNo(refund.getOutRefundNo());

		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setNotifyUrl(configInfo.getNotifyUrl());
		request.setBizModel(model);

		AlipayTradeFastpayRefundQueryResponse response;
		try {
			response = client.execute(request);
		}
		catch (AlipayApiException e) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, "Alipay退款查询接口失败, AlipayApiException,"
					+ " #errCode: " + e.getErrCode() + " #errMsg: " + e.getErrMsg());
		}

		if (!response.isSuccess()) {
			throw new PayException(PayErrCode.CLIENT_API_ERR, errMsg("Alipay退款查询接口失败", response));
		}

		return RefundQueryResponse.builder()
				.transactionId(response.getTradeNo())
				.outTradeNo(response.getOutTradeNo())
				.outRefundNo(response.getOutRequestNo())
				.refundStatus(translateRefundStatus(response))
				.build();
	}

	private static String genPrePayId() {
		return "alipay" + UUID.randomUUID().toString().replace("-", "");
	}

	private static String errMsg(String action, AlipayResponse response) {
		return action + ","
				+ " #code:" + response.getCode()
				+ " #msg: " + response.getMsg()
				+ " #sub_code: " + response.getSubCode()
				+ " #sub_msg: " + response.getSubMsg();
	}

	private static String translatePayStatus(String tradeState) {
		if (TRADE_STATUS_SUCCESS.equals(tradeState)) {
			return Constants.Pay.SUCCESS;
		}
		if (TRADE_STATUS_WAIT_BUYER_PAY.equals(tradeState)) {
			return Constants.Pay.WAIT_PAY;
		}
		if (TRADE_STATUS_TRADE_CLOSED.equals(tradeState)) {
			return Constants.Pay.CLOSED;
		}
		if (TRADE_STATUS_TRADE_FINISHED.equals(tradeState)) {
			return Constants.Pay.CLOSED;
		}
		return Constants.Pay.UNKNOWN;
	}

	private static String translateRefundStatus(AlipayTradeFastpayRefundQueryResponse response) {
		String outTradeNo = response.getOutTradeNo();
		String outRequestNo = response.getOutRequestNo();
		String refundAmount = response.getRefundAmount();
		return StringUtils.isNotBlank(outTradeNo)
				&& StringUtils.isNotBlank(outRequestNo)
				&& StringUtils.isNotBlank(refundAmount)
				? Constants.Refund.SUCCESS
				: Constants.Refund.PROCESSING;
	}

}
