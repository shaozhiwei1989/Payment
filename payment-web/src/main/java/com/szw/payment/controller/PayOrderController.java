package com.szw.payment.controller;

import static com.szw.payment.common.AliPayKeys.RESULT_FAILURE;
import static com.szw.payment.common.AliPayKeys.RESULT_SUCCESS;
import static com.szw.payment.common.AliPayKeys.TRADE_STATUS_SUCCESS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;

import com.szw.payment.Result;
import com.szw.payment.ResultCode;
import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CheckAliPaySignRequest;
import com.szw.payment.api.model.CompletePayRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.api.service.PayOrderService;
import com.szw.payment.bean.PrepayBean;
import com.szw.payment.common.AliPayKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pay")
@Slf4j(topic = "running")
public class PayOrderController {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@DubboReference
	private PayOrderService payOrderService;


	@PostMapping("/prepay")
	public Result<PayOrderResponse> prepay(@RequestBody PrepayBean prepayBean) {
		try {
			PayOrderCreateRequest request = new PayOrderCreateRequest();
			request.setTradeId(prepayBean.getTradeId());
			request.setChannel(prepayBean.getChannel());
			request.setUserId(prepayBean.getUserId());

			// 需要查询订单信息
			request.setBody("测试11");
			request.setTotalFee(10L);
			ServiceResponse<PayOrderResponse> response = payOrderService.createPayOrder(request);
			return new Result<>(response.getCode(), response.getDesc(), response.getData());
		}
		catch (Exception e) {
			log.error("prepay", e);
			return new Result<>(ResultCode.ERROR, "接口异常");
		}
	}

	@RequestMapping("/alipay/notice")
	public String alipayNotice(@RequestParam Map<String, String> paramsMap) {
		try {
			if (!checkAliPaySign(paramsMap)) {
				log.info("签名验证失败:map {}", paramsMap);
				return RESULT_FAILURE;
			}

			String tradeStatus = paramsMap.get(AliPayKeys.TRADE_STATUS);
			if (!TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
				log.info("非支付成功状态: map {}", paramsMap);
				// 忽略 非支付成功状态 的回调，返回成功 避免请求重推
				return RESULT_SUCCESS;
			}

			String outTradeNo = paramsMap.get(AliPayKeys.OUT_TRADE_NO);
			String transactionId = paramsMap.get(AliPayKeys.TRADE_NO);
			String gmtPayment = paramsMap.get(AliPayKeys.GMT_PAYMENT);

			LocalDateTime payTime;
			try {
				payTime = LocalDateTime.parse(gmtPayment, formatter);
			}
			catch (DateTimeParseException e) {
				payTime = LocalDateTime.now();
				log.error("", e);
			}

			CompletePayRequest request = new CompletePayRequest();
			request.setPayTime(payTime);
			request.setOutTradeNo(outTradeNo);
			request.setTransactionId(transactionId);
			ServiceResponse<Boolean> response = payOrderService.completePay(request);
			return Objects.equals(response.getData(), true) ? RESULT_SUCCESS : RESULT_FAILURE;
		}
		catch (Exception e) {
			log.error("alipayNotice", e);
			return RESULT_FAILURE;
		}
	}

	private boolean checkAliPaySign(Map<String, String> paramsMap) {
		CheckAliPaySignRequest request = new CheckAliPaySignRequest();
		request.setAppId(paramsMap.get(AliPayKeys.APP_ID));
		request.setCheckParamsMap(paramsMap);
		ServiceResponse<Boolean> response = payOrderService.checkAliPaySign(request);
		return Objects.equals(response.getData(), true);
	}

}
