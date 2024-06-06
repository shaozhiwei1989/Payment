package com.szw.payment.test;

import java.time.LocalDateTime;

import com.szw.payment.bootstrap.Application;
import com.szw.payment.entity.RefundOrder;
import com.szw.payment.store.RefundOrderStore;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class RefundOrderStoreTest {

	@Inject
	private RefundOrderStore refundOrderStore;

	@Test
	public void testSave() {
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setAmount(1L);
		refundOrder.setConfigId(2L);
		refundOrder.setPayOrderId(3L);
		refundOrder.setUserId("test_user_id_1");
		refundOrder.setTradeId("test_trade_id_1");
		refundOrder.setOutRefundNo("test_out_refund_no_1");
		refundOrder.setStatus(0);
		refundOrder.setIdempotentKey("test_idempotent_key_1");
		refundOrder.setRetries(0);
		refundOrderStore.save(refundOrder);
	}

	@Test
	public void testFindByOutRefundNo() {
		RefundOrder refundOrder = refundOrderStore.findByOutRefundNo("test_out_refund_no_1");
		System.out.print(refundOrder + "\n");
	}

	@Test
	public void testCountByIdempotentKey() {
		int count = refundOrderStore.countByIdempotentKey("test_idempotent_key_1");
		System.out.print(count + "\n");
	}

	@Test
	public void testUpdateRefundToIng() {
		int rows = refundOrderStore.updateRefundToIng(1L, 0, 10, LocalDateTime.now());
		System.out.print(rows + "\n");
	}

	@Test
	public void testUpdateRefundToComplete() {
		int rows = refundOrderStore.updateRefundToComplete(1L, 10, 20, LocalDateTime.now(), null);
		System.out.print(rows + "\n");
	}

	@Test
	public void testUpdateRefundToRetry() {
		int rows = refundOrderStore.updateRefundToRetry(1L, 20, 30, "测试失败", 1);
		System.out.print(rows + "\n");
	}

}
