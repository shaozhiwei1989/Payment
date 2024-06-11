package com.szw.payment.archive.task;

import java.time.LocalDateTime;
import java.util.List;

import com.szw.payment.archive.entity.Archive;
import com.szw.payment.archive.manager.MongoManager;
import com.szw.payment.archive.manager.MysqlManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

import org.springframework.beans.factory.annotation.Value;

@Named("archive_task")
@Slf4j(topic = "running")
public class ArchiveTask implements SimpleJob {

	@Inject
	private MysqlManager mysqlManager;

	@Inject
	private MongoManager mongoManager;

	@Value("${archive-time:365}")
	private Long archiveTime;


	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("-----ArchiveTask start------");

		int shardingItem = shardingContext.getShardingItem();
		int shardingTotal = shardingContext.getShardingTotalCount();

		long startId = 0L;
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(archiveTime);
		for (; ; ) {
			List<Archive> archives = mysqlManager.scanArchive(startId, localDateTime, shardingTotal, shardingItem);
			if (archives == null || archives.isEmpty()) {
				break;
			}

			for (Archive archive : archives) {
				boolean success;
				try {
					mongoManager.transferToArchive(archive);
					success = true;
				}
				catch (Exception e) {
					success = false;
					log.info("ArchiveTask#迁移异常:{}", archive);
					log.error("ArchiveTask", e);
				}
				if (success) {
					mysqlManager.deleteArchiveIfTransferSuccess(archive);
				}
			}

			startId = archives.getLast().getPayOrder().getId();
		}

		log.info("-----ArchiveTask end------");
	}

}
