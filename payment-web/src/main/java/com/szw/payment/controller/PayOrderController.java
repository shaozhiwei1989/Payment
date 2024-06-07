package com.szw.payment.controller;

import com.szw.payment.Result;
import com.szw.payment.ResultCode;
import com.szw.payment.api.ServiceResponse;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.model.PayOrderResponse;
import com.szw.payment.api.service.PayOrderService;
import com.szw.payment.bean.PrepayBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pay")
@Slf4j(topic = "running")
public class PayOrderController {

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

}
