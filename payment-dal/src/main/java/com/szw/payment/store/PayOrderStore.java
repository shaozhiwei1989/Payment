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
				      `status` = :toStatus,
				      `version` = `version` + 1
				where out_trade_no = :outTradeNo
				  and `status` = :fromStatus
				  and `version` = :version
			""")
	int completePay(String transactionId, LocalDateTime payDoneTime,
			String outTradeNo, String fromStatus, String toStatus, long version);


	@Modifying
	@Query("""
				update pay_order
				  set balance = balance - :amount,
				      refund_frozen_Amount = refund_frozen_Amount + :amount,
				      `version` = `version` + 1
				where id = :id
				  and balance >= :amount
				  and `version` = :version
			""")
	int freezeRefundAmount(Long id, long amount, long version);

	@Modifying
	@Query("""
				update pay_order
				  set refund_frozen_Amount = refund_frozen_Amount - :amount,
				      refund_amount = refund_amount + :amount,
				      `version` = `version` + 1
				where id = :id
				  and refund_frozen_Amount >= :amount
				  and `version` = :version
			""")
	int completeRefund(Long id, long amount, long version);

	@Modifying
	@Query("""
				update pay_order
				  set refund_frozen_Amount = refund_frozen_Amount - :amount,
				      balance = balance + :amount,
				      `version` = `version` + 1
				where id = :id
				  and refund_frozen_Amount >= :amount
				  and `version` = :version
			""")
	int rollbackRefund(Long id, long amount, long version);

}
