package com.szw.payment.test;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.alibaba.druid.pool.DruidDataSource;
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

	@Inject
	private DataSource dataSource;

	@Test
	public void test1() {
		Random random = new Random();
		for (int i = 300100; i < 300101; i++) {
			PayOrderCreateRequest request = new PayOrderCreateRequest();
			request.setUserId("test_user_id_1");
			request.setTradeId("test_trade_id_10000_" + i);
			request.setChannel("mock_pay");
			request.setTotalFee((long) random.nextInt(1, 100));
			request.setBody("test_body_10000_" + i);
			payOrderService.createPayOrder(request);
		}
	}

	@Test
	public void test2() {
		payOrderManager.completePay("test_trade_id_10000_8-4", System.currentTimeMillis() + "", LocalDateTime.now());
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

//	@Test
//	public void test6() {
//		QueryRefundOrderRequest request = new QueryRefundOrderRequest();
//		request.setIdempotentKey("test4_1");
//		ServiceResponse<QueryRefundOrderResponse> response = refundOrderService.queryRefundOrder(request);
//		System.out.printf(response + "\n");
//		System.out.printf(GsonUtil.GSON.toJson(response) + "\n");
//	}

	@Test
	public void test7() {
		DruidDataSource druidDataSource = (DruidDataSource) dataSource;
		System.out.printf(druidDataSource + "\n");
		System.out.printf(druidDataSource.getMinIdle() + "\n");
	}

}
