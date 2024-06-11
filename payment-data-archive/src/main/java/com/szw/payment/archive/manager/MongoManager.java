package com.szw.payment.archive.manager;

import java.util.List;

import com.szw.payment.archive.entity.Archive;
import com.szw.payment.archive.entity.RefundOrder;
import com.szw.payment.archive.store.mongo.PayOrderStore;
import com.szw.payment.archive.store.mongo.RefundOrderStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class MongoManager {

	@Inject
	@Named("pay_order_mongo_store")
	private PayOrderStore payOrderStore;

	@Inject
	@Named("refund_order_mongo_store")
	private RefundOrderStore refundOrderStore;


	public void transferToArchive(Archive archive) {
		if (archive == null) {
			return;
		}

		if (archive.getPayOrder() != null) {
			payOrderStore.save(archive.getPayOrder());
		}

		List<RefundOrder> refundOrderList = archive.getRefundOrderList();
		if (refundOrderList != null && !refundOrderList.isEmpty()) {
			refundOrderStore.saveAll(refundOrderList);
		}
	}

}
