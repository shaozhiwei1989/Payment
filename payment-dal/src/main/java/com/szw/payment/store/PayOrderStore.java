package com.szw.payment.store;

import com.szw.payment.entity.PayOrder;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface PayOrderStore extends Repository<PayOrder, Long> {

	void save(PayOrder payOrder);

	PayOrder findById(Long id);

	PayOrder findByTradeIdAndChannel(String tradeId, String channel);

	PayOrder findByOutTradeNo(String outTradeNo);

	@Modifying
	@Query("""
				update pay_order
				  set transaction_id = :#{#payOrder.transactionId},
				      pay_done_time = :#{#payOrder.payDoneTime},
				      `status` = :#{#payOrder.status}
				where id = :#{#payOrder.id}
				  and `status` = :fromStatus
			""")
	int completePay(PayOrder payOrder, String fromStatus);

	@Modifying
	@Query("""
				update pay_order
				  set balance = balance - :amount,
				      refund_frozen_amount = refund_frozen_amount + :amount
				where id = :id
				  and balance >= :amount
			""")
	int freezeRefundAmount(Long id, long amount);

	@Modifying
	@Query("""
				update pay_order
				  set refund_frozen_amount = refund_frozen_amount - :amount,
				      refund_amount = refund_amount + :amount
				where id = :id
				  and refund_frozen_Amount >= :amount
			""")
	int completeRefund(Long id, long amount);

	@Modifying
	@Query("""
				update pay_order
				  set refund_frozen_amount = refund_frozen_amount - :amount,
				      balance = balance + :amount
				where id = :id
				  and refund_frozen_Amount >= :amount
			""")
	int rollbackRefund(Long id, long amount);

}
