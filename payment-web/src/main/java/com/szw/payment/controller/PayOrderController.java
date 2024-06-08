package com.szw.payment.controller;


import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import com.google.gson.reflect.TypeToken;
import com.szw.payment.Result;
import com.szw.payment.ResultCode;
import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.CompleteForAlipayRequest;
import com.szw.payment.api.model.CompleteForWxpayRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.api.service.PayOrderService;
import com.szw.payment.bean.PrepayBean;
import com.szw.payment.common.AliPayKeys;
import com.szw.payment.common.WxPayKeys;
import com.szw.payment.common.utils.GsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pay")
@Slf4j(topic = "running")
public class PayOrderController {
	private static final Type TYPE_OF_MAP = new TypeToken<Map<String, Object>>() {}.getType();

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
			CompleteForAlipayRequest request = new CompleteForAlipayRequest();
			request.setParamsMap(paramsMap);
			ServiceResponse<Boolean> response = payOrderService.completeForAlipay(request);
			boolean isTrue = Objects.equals(response.getData(), true);
			return isTrue ? AliPayKeys.RESULT_SUCCESS : AliPayKeys.RESULT_FAILURE;
		}
		catch (Exception e) {
			log.error("alipayNotice", e);
			return AliPayKeys.RESULT_FAILURE;
		}
	}

	@RequestMapping("/wxpay/notice/{appId}/{mchId}")
	public String wxpayNotice(HttpServletRequest httpServletRequest,
			@PathVariable("appId") String appId,
			@PathVariable("mchId") String mchId,
			@RequestBody String body) {

		try {
			Map<String, Object> bodyMap = GsonUtil.GSON.fromJson(body, TYPE_OF_MAP);
			String eventType = (String) bodyMap.get(WxPayKeys.EVENT_TYPE);
			if (!Objects.equals(eventType, WxPayKeys.PAY_SCORE_USER_PAID)) {
				log.info("非支付成功回调不处理 body:{}", body);
				// 忽略 非支付成功 的回调，返回成功 避免请求重推
				return WxPayKeys.RESULT_SUCCESS;
			}

			String nonce = httpServletRequest.getHeader(WxPayKeys.NONCE);
			String signature = httpServletRequest.getHeader(WxPayKeys.SIGNATURE);
			String timeStamp = httpServletRequest.getHeader(WxPayKeys.TIMESTAMP);
			String serialNumber = httpServletRequest.getHeader(WxPayKeys.SERIAL);

			CompleteForWxpayRequest request = new CompleteForWxpayRequest();
			request.setAppId(appId);
			request.setMchId(mchId);
			request.setBody(body);
			request.setNonce(nonce);
			request.setSignature(signature);
			request.setTimestamp(timeStamp);
			request.setSerialNumber(serialNumber);
			ServiceResponse<Boolean> response = payOrderService.completeForWxPay(request);
			boolean isTrue = Objects.equals(response.getData(), true);
			return isTrue ? WxPayKeys.RESULT_SUCCESS : WxPayKeys.RESULT_FAILURE;
		}
		catch (Exception e) {
			log.error("wxpayNotice", e);
			return WxPayKeys.RESULT_FAILURE;
		}
	}

}
