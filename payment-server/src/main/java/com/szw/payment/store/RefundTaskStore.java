package com.szw.payment.store;

import java.time.LocalDateTime;
import java.util.List;

import com.szw.payment.entity.RefundTask;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface RefundTaskStore extends Repository<RefundTask, Long> {

	void save(RefundTask refundTask);

	void deleteById(Long id);

	@Query("""
				select *
				 from refund_task
				where id > :currentId
				 and exec_time <= now()
				 and mod(id,:shardTotal) = :shardItem
				order by id asc
				 limit 50
			""")
	List<RefundTask> findTop50Tasks(long currentId, int shardTotal, int shardItem);

	@Modifying
	@Query("delete from refund_task where refund_order_id = :refundOrderId")
	void deleteByRefundOrderId(Long refundOrderId);

	@Modifying
	@Query("update refund_task set exec_time = :execTime where refund_order_id = :refundOrderId")
	void updateExecTime(Long refundOrderId, LocalDateTime execTime);

}
