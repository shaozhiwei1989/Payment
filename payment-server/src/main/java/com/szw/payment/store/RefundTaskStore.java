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

	List<RefundTask> findTop50ByExecTimeLessThanEqualOrderByIdAsc(LocalDateTime time);

	@Modifying
	@Query("update refund_task set exec_time = :execTime where refund_order_id = :refundOrderId")
	void updateExecTime(Long refundOrderId, LocalDateTime execTime);

}
