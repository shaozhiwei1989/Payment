package com.szw.payment.store;

import com.szw.payment.entity.RefundOrder;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface RefundOrderStore extends Repository<RefundOrder, Long> {

	RefundOrder findById(Long id);

	void save(RefundOrder refundOrder);

	RefundOrder findByOutRefundNo(String outRefundNo);

	int countByIdempotentKey(String idempotentKey);

	@Modifying
	@Query("""
				update refund_order
				  set refund_create_time = now(),
			          `status` = :toStatus
			    where id = :id
			     and `status` = :fromStatus
			""")
	int updateRefundToIng(Long id, int fromStatus, int toStatus);

	@Modifying
	@Query("""
				update refund_order
				  set refund_create_time = ifnull(refund_create_time,now()),
				      retries = :retries,
			          `status` = :toStatus,
			          refund_fail_desc = :failDesc
			    where id = :id
			     and `status` = :fromStatus
			""")
	int updateRefundToRetry(Long id, int fromStatus, int toStatus, String failDesc, int retries);

	@Modifying
	@Query("""
				update refund_order
				 set `status` = :#{#refundOrder.status},
				      refund_end_time = :#{#refundOrder.refundEndTime},
				      refund_fail_desc = :#{#refundOrder.refundFailDesc}
				where id = :#{#refundOrder.id}
				 and `status` = :fromStatus
			""")
	int updateRefundToComplete(RefundOrder refundOrder, int fromStatus);

}
