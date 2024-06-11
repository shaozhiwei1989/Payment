package com.szw.payment.store;

import java.time.LocalDateTime;

import com.szw.payment.entity.RefundOrder;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface RefundOrderStore extends Repository<RefundOrder, Long> {

	void save(RefundOrder refundOrder);

	RefundOrder findByOutRefundNo(String outRefundNo);

	int countByIdempotentKey(String idempotentKey);

	@Modifying
	@Query("""
				update refund_order
				  set refund_create_time = :refundCreateTime,
			          `status` = :toStatus
			    where id = :id
			     and `status` = :fromStatus
			""")
	int updateRefundToIng(Long id, int fromStatus, int toStatus, LocalDateTime refundCreateTime);

	@Modifying
	@Query("""
				update refund_order
				  set retries = :retries,
			          `status` = :toStatus,
			          refund_fail_desc = :failDesc
			    where id = :id
			     and `status` = :fromStatus
			""")
	int updateRefundToRetry(Long id, int fromStatus, int toStatus, String failDesc, int retries);

	@Modifying
	@Query("""
				update refund_order
				 set `status` = :toStatus,
				      refund_end_time = :refundEndTime,
				      refund_fail_desc = :failDesc
				where id = :id
				 and `status` = :fromStatus
			""")
	int updateRefundToComplete(Long id, int fromStatus, int toStatus, LocalDateTime refundEndTime, String failDesc);

}
