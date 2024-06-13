package com.szw.payment.test;

import java.time.LocalDateTime;
import java.util.List;

import com.szw.payment.api.model.CreateRefundOrderRequest;
import com.szw.payment.api.model.PayOrderCreateRequest;
import com.szw.payment.api.service.PayOrderService;
import com.szw.payment.api.service.RefundOrderService;
import com.szw.payment.bootstrap.Application;
import com.szw.payment.entity.RefundTask;
import com.szw.payment.manager.PayOrderManager;
import com.szw.payment.manager.RefundTaskManager;
import com.szw.payment.store.RefundTaskStore;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class Test1 {


	@Inject
	private PayOrderService payOrderService;

	@Inject
	private PayOrderManager payOrderManager;

	@Inject
	private RefundOrderService refundOrderService;

	@Inject
	private RefundTaskManager refundTaskManager;

	@Inject
	private RefundTaskStore refundTaskStore;

	@Test
	public void test1() {
		PayOrderCreateRequest request = new PayOrderCreateRequest();
		request.setUserId("test_user_id_1");
		request.setTradeId("test_trade_id_1");
		request.setChannel("mock_pay");
		request.setTotalFee(50L);
		request.setBody("test_body_1");
		payOrderService.createPayOrder(request);
	}

	@Test
	public void test2() {
		payOrderManager.completePay("test_trade_id_1-4", System.currentTimeMillis() + "", LocalDateTime.now());
	}

	@Test
	public void test3() {
		CreateRefundOrderRequest request = new CreateRefundOrderRequest();
		request.setAmount(10L);
		request.setIdempotentKey("test3_1");
		request.setChannel("mock_pay");
		request.setTradeId("test_trade_id_1");
		request.setUserId("test_user_id_1");
		request.setDescription("test3_1_desc");
		request.getPassBackParamMap().put("test_1", "test_1");
		refundOrderService.createRefundOrder(request);
	}

	@Test
	public void test4() {
		for (int i = 10; i < 15; i++) {
			CreateRefundOrderRequest request = new CreateRefundOrderRequest();
			request.setAmount(10L);
			request.setIdempotentKey(String.format("test%s_1", i));
			request.setChannel("mock_pay");
			request.setTradeId("test_trade_id_1");
			request.setUserId("test_user_id_1");
			request.setDescription("test5_1_desc");
			request.getPassBackParamMap().put("test_5", "test_5");
			refundOrderService.createRefundOrder(request);
		}
	}

	@Test
	public void test5() {
		List<RefundTask> tasks = refundTaskManager.scanTasks(0L, 1, 0);
//		refundTaskManager.executeRefundTask(tasks.getFirst());

		Long refundOrderId = tasks.getFirst().getRefundOrderId();
		refundTaskStore.deleteByRefundOrderId(refundOrderId);
	}

}
