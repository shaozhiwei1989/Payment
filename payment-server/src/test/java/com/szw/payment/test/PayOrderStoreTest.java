package com.szw.payment.test;

import com.szw.payment.bootstrap.Application;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.store.PayOrderStore;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class PayOrderStoreTest {

	@Inject
	private PayOrderStore payOrderStore;


	@Test
	public void testSave() {
		PayOrder payOrder = new PayOrder();
		payOrder.setChannel("wx_app");
		payOrder.setOutTradeNo("out_trade_no_test_1");
		payOrder.setConfigId(1L);
		payOrder.setAmount(10L);
		payOrder.setBalance(10L);
		payOrder.setRefundAmount(0L);
		payOrder.setRefundFrozenAmount(0L);
		payOrder.setUserId("user_id_test_1");
		payOrder.setTradeId("trade_id_test_1");
		payOrder.setStatus("not_pay");
		payOrder.putExtraParam("test_key", "test_value")
				.generateExtraParam();

		payOrderStore.save(payOrder);
	}

	@Test
	public void testFindById() {
		PayOrder payOrder = payOrderStore.findById(1L);
		System.out.print(payOrder + "\n");
	}

	@Test
	public void testFindByTradeIdAndChannel() {
		PayOrder payOrder = payOrderStore.findByTradeIdAndChannel("trade_id_test_1", "wx_app");
		System.out.print(payOrder + "\n");
	}

	@Test
	public void testFindByOutTradeNo() {
		PayOrder payOrder = payOrderStore.findByOutTradeNo("out_trade_no_test_1");
		System.out.print(payOrder + "\n");
	}

//	@Test
//	public void testCompletePay() {
//		PayOrder payOrder = payOrderStore.findById(1L);
//		int rows = payOrderStore.completePay("test", LocalDateTime.now(),
//				payOrder.getOutTradeNo(), "not_pay", "success",
//				payOrder.getVersion());
//
//		System.out.print(rows + "\n");
//	}

//	@Test
//	public void testFreezeRefundAmount() {
//		PayOrder payOrder = payOrderStore.findById(1L);
//		int rows = payOrderStore.freezeRefundAmount(payOrder.getId(), 2, payOrder.getVersion());
//		System.out.print(rows + "\n");
//	}
//
//	@Test
//	public void testCompleteRefund() {
//		PayOrder payOrder = payOrderStore.findById(1L);
//		int rows = payOrderStore.completeRefund(payOrder.getId(), 1, payOrder.getVersion());
//		System.out.print(rows + "\n");
//	}
//
//
//	@Test
//	public void testRollbackRefund() {
//		PayOrder payOrder = payOrderStore.findById(1L);
//		int rows = payOrderStore.rollbackRefund(payOrder.getId(), 1, payOrder.getVersion());
//		System.out.print(rows + "\n");
//	}

}
