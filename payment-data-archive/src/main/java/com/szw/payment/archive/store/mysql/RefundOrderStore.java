package com.szw.payment.archive.store.mysql;


import java.util.Collection;
import java.util.List;

import com.szw.payment.archive.entity.RefundOrder;
import jakarta.inject.Named;

import org.springframework.data.repository.CrudRepository;

@Named("refund_order_mysql_store")
public interface RefundOrderStore extends CrudRepository<RefundOrder, Long> {

	List<RefundOrder> findByPayOrderIdIn(Collection<Long> payOrderIds);

}
