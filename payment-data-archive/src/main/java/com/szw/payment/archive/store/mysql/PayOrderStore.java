package com.szw.payment.archive.store.mysql;


import java.time.LocalDateTime;
import java.util.List;

import com.szw.payment.archive.entity.PayOrder;
import jakarta.inject.Named;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

@Named("pay_order_mysql_store")
public interface PayOrderStore extends CrudRepository<PayOrder, Long> {

	@Query("""
				select * from pay_order
				 where id > :startId
				  and create_time <= :archiveTime
				  and mod(id,:shardTotal) = :shardItem
				 order by id asc
				  limit 50
			""")
	List<PayOrder> findTop50SortByIdAsc(long startId,
			LocalDateTime archiveTime, int shardTotal, int shardItem);

}
