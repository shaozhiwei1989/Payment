package com.szw.payment.test;

import java.time.LocalDateTime;
import java.util.List;

import com.szw.payment.bootstrap.Application;
import com.szw.payment.entity.RefundTask;
import com.szw.payment.store.RefundTaskStore;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class RefundTaskStoreTest {

	@Inject
	private RefundTaskStore refundTaskStore;


	@Test
	public void testSave() {
		for (int i = 3; i < 15; i++) {
			RefundTask refundTask = new RefundTask();
			refundTask.setRefundOrderId((long) i);
			refundTask.setExecTime(LocalDateTime.now());
			refundTask.setUserId("test_user_id");
			refundTask.setTradeId("test_trade_id");
			refundTaskStore.save(refundTask);
		}
	}


	@Test
	public void testFindTop50ByExecTimeLessThanEqualOrderByIdAsc() {
		List<RefundTask> list = refundTaskStore.findTop50ByExecTimeLessThanEqualOrderByIdAsc(LocalDateTime.now());
		System.out.print(list + "\n");
	}

	@Test
	public void updateExecTime(){
		refundTaskStore.updateExecTime(1L,LocalDateTime.now());
	}

	@Test
	public void testDeleteById(){
		refundTaskStore.deleteById(14L);
	}

}
