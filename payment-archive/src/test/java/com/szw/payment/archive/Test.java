package com.szw.payment.archive;

import java.time.LocalDateTime;
import java.util.List;

import com.szw.payment.archive.bootstrap.Application;
import com.szw.payment.archive.entity.Archive;
import com.szw.payment.archive.manager.MongoManager;
import com.szw.payment.archive.manager.MysqlManager;
import jakarta.inject.Inject;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class Test {

	@Inject
	private MongoManager mongoManager;

	@Inject
	private MysqlManager mysqlManager;


	@org.junit.jupiter.api.Test
	public void test() {

//		Optional<PayOrder> optional = payOrderStore.findById(6L);

//		List<RefundOrder> refundOrders = refundOrderStore.findByPayOrderIdIn(List.of(7L, 9L));
//		System.out.printf(refundOrders + "\n");

//		PayOrderArchive payOrderArchive = new PayOrderArchive();
//		payOrderArchive.setPayOrder(optional.get());
//		payOrderArchive.setRefundOrders(refundOrders);
//		payOrderArchiveStore.(payOrderArchive);
//		optional.get().setChannel("wx_mini");

		List<Archive> archives = mysqlManager.scanArchive(0, LocalDateTime.now(), 1, 0);
		System.out.printf("archives" + archives + "\n");

		archives.forEach(mongoManager::transferToArchive);
	}

}
