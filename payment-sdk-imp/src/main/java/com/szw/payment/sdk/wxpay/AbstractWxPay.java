package com.szw.payment.sdk.wxpay;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.szw.payment.common.Constants;
import com.szw.payment.common.WxPayKeys;
import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.common.model.RefundCreateResponse;
import com.szw.payment.common.model.RefundQueryResponse;
import com.szw.payment.sdk.Pay;
import com.szw.payment.sdk.exception.PayErrCode;
import com.szw.payment.sdk.exception.PayException;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.Status;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public abstract class AbstractWxPay implements Pay {
	private static final Map<String /* 商户号id */, Config> wxConfigCache = new ConcurrentHashMap<>();
	private static final Set<String> retryCode = Set.of("NOT_ENOUGH", "FREQUENCY_LIMITED", "SYSTEM_ERROR");

	protected ConfigInfo configInfo;
	protected Config wxConfig;


	@Override
	public final void initConfig(ConfigInfo configInfo) throws Exception {
		this.configInfo = configInfo;
		this.wxConfig = wxConfigCache.computeIfAbsent(configInfo.getMchId(),
				mchId -> new RSAAutoCertificateConfig.Builder()
						.merchantId(configInfo.getMchId())
						.privateKey(configInfo.getPrivateKey())
						.merchantSerialNumber(configInfo.getMchSerialNumber())
						.apiV3Key(configInfo.getApiKey())
						.build());
	}

	@Override
	public RefundCreateResponse createRefund(com.szw.payment.common.model.Refund refund) {
		CreateRequest request = new CreateRequest();
		request.setNotifyUrl(configInfo.getRefundUrl());
		request.setOutRefundNo(refund.getOutRefundNo());
		request.setOutTradeNo(refund.getOutTradeNo());
		request.setReason(refund.getRefundDesc());
		request.setTransactionId(refund.getTransactionId());

		AmountReq amountReq = new AmountReq();
		amountReq.setTotal(refund.getTotalFee());
		amountReq.setRefund(refund.getRefundFee());
		amountReq.setCurrency("CNY");
		request.setAmount(amountReq);

		RefundService service = new RefundService.Builder().config(wxConfig).build();
		Refund response;
		try {
			response = service.create(request);
		}
		catch (ServiceException e) {
			if (retryCode.contains(e.getErrorCode())) {
				throw new PayException(PayErrCode.RETRY_LATER, e.getErrorMessage());
			}
			throw new PayException(PayErrCode.CLIENT_API_ERR, e.getErrorMessage());
		}
		return RefundCreateResponse.builder()
				.outRefundNo(response.getOutRefundNo())
				.transactionId(response.getTransactionId())
				.waitCallBack(true)
				.build();
	}


	@Override
	public RefundQueryResponse queryRefundOrder(com.szw.payment.common.model.Refund refund) {
		QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
		request.setOutRefundNo(refund.getOutRefundNo());

		RefundService service = new RefundService.Builder().config(wxConfig).build();
		Refund response = service.queryByOutRefundNo(request);

		String refundStatus = translateRefundStatus(response.getStatus());
		return RefundQueryResponse.builder()
				.outRefundNo(response.getOutRefundNo())
				.transactionId(response.getTransactionId())
				.refundStatus(refundStatus)
				.build();
	}

	public static LocalDateTime convertStrToLocalDateTime(String dateStr) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		return LocalDateTime.parse(dateStr, WxPayKeys.formatter);
	}

	public static String translatePayStatus(Transaction.TradeStateEnum tradeState) {
		return switch (tradeState) {
			case SUCCESS -> Constants.Pay.SUCCESS;
			case NOTPAY -> Constants.Pay.WAIT_PAY;
			case CLOSED -> Constants.Pay.CLOSED;
			default -> Constants.Pay.UNKNOWN;
		};
	}

	public static String translateRefundStatus(Status status) {
		return switch (status) {
			case SUCCESS -> Constants.Refund.SUCCESS;
			case PROCESSING -> Constants.Refund.PROCESSING;
			default -> Constants.Refund.ERROR;
		};
	}

}
