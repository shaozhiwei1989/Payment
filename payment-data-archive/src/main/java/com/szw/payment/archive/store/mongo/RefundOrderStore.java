package com.szw.payment.archive.store.mongo;

import com.szw.payment.archive.entity.RefundOrder;
import jakarta.inject.Named;

import org.springframework.data.mongodb.repository.MongoRepository;

@Named("refund_order_mongo_store")
public interface RefundOrderStore extends MongoRepository<RefundOrder, Long> {

}
