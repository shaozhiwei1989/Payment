package com.szw.payment.store;

import java.time.LocalDateTime;

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
				  set transaction_id = :transactionId,
				      pay_done_time = :payDoneTime,
				      `status` = :toStatus
				where id = :id
				  and `status` = :fromStatus
			""")
	int completePay(Long id, String fromStatus, String toStatus,
			String transactionId, LocalDateTime payDoneTime);


	@Modifying
	@Query("""
				update pay_order
				  set balance = balance - :amount,
				      refund_frozen_Amount = refund_frozen_Amount + :amount
				where id = :id
				  and balance >= :amount
			""")
	int freezeRefundAmount(Long id, long amount);

	@Modifying
	@Query("""
				update pay_order
				  set refund_frozen_Amount = refund_frozen_Amount - :amount,
				      refund_amount = refund_amount + :amount
				where id = :id
				  and refund_frozen_Amount >= :amount
			""")
	int completeRefund(Long id, long amount);

	@Modifying
	@Query("""
				update pay_order
				  set refund_frozen_Amount = refund_frozen_Amount - :amount,
				      balance = balance + :amount
				where id = :id
				  and refund_frozen_Amount >= :amount
			""")
	int rollbackRefund(Long id, long amount);

}
