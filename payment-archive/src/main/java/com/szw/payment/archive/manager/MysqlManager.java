package com.szw.payment.archive.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.szw.payment.archive.entity.Archive;
import com.szw.payment.archive.entity.PayOrder;
import com.szw.payment.archive.entity.RefundOrder;
import com.szw.payment.archive.store.mysql.PayOrderStore;
import com.szw.payment.archive.store.mysql.RefundOrderStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.springframework.transaction.annotation.Transactional;

@Named
public class MysqlManager {

	@Inject
	@Named("pay_order_mysql_store")
	private PayOrderStore payOrderStore;

	@Inject
	@Named("refund_order_mysql_store")
	private RefundOrderStore refundOrderStore;


	public List<Archive> scanArchive(long startId, LocalDateTime archiveTime, int shardTotal, int shardItem) {
		List<PayOrder> list = payOrderStore.findTop50SortByIdAsc(startId, archiveTime, shardTotal, shardItem);
		if (list == null || list.isEmpty()) {
			return new ArrayList<>();
		}

		Map<Long /* PayOrderId */, List<RefundOrder>> map = new HashMap<>(list.size());
		List<Long> ids = list.stream().map(PayOrder::getId).toList();
		List<RefundOrder> refundOrders = refundOrderStore.findByPayOrderIdIn(ids);
		if (refundOrders != null && !refundOrders.isEmpty()) {
			refundOrders.forEach(refundOrder -> {
				Long id = refundOrder.getPayOrderId();
				map.computeIfAbsent(id, k -> new ArrayList<>()).add(refundOrder);
			});
		}
		return list.stream().map(payOrder -> new Archive(payOrder, map.get(payOrder.getId()))).toList();
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteArchiveIfTransferSuccess(Archive archive) {
		if (archive == null) {
			return;
		}
		if (archive.getPayOrder() != null) {
			payOrderStore.delete(archive.getPayOrder());
		}

		List<RefundOrder> refundOrderList = archive.getRefundOrderList();
		if (refundOrderList != null && !refundOrderList.isEmpty()) {
			refundOrderStore.deleteAll(refundOrderList);
		}
	}

}
