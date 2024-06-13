package com.szw.payment.task;

import java.util.List;

import com.szw.payment.entity.RefundTask;
import com.szw.payment.manager.RefundTaskManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

@Named("execute_refund_task")
@Slf4j(topic = "running")
public class ExecuteRefundTask implements SimpleJob {

	@Inject
	private RefundTaskManager refundTaskManager;


	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("-----ExecuteRefundTask start------");

		int shardingItem = shardingContext.getShardingItem();
		int shardingTotal = shardingContext.getShardingTotalCount();

		long currentId = 0L;
		for (; ; ) {
			List<RefundTask> tasks = refundTaskManager.scanTasks(currentId, shardingTotal, shardingItem);
			if (tasks == null || tasks.isEmpty()) {
				break;
			}

			tasks.forEach(this::execute);
			currentId = tasks.getLast().getId();
		}

		log.info("-----ExecuteRefundTask end------");
	}

	private void execute(RefundTask task) {
		try {
			refundTaskManager.executeRefundTask(task);
		}
		catch (Exception e) {
			log.error("", e);
		}
	}

}
