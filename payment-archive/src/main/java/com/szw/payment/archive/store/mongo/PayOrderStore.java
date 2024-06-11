package com.szw.payment.archive.store.mongo;

import com.szw.payment.archive.entity.PayOrder;
import jakarta.inject.Named;

import org.springframework.data.mongodb.repository.MongoRepository;

@Named("pay_order_mongo_store")
public interface PayOrderStore extends MongoRepository<PayOrder, Long> {

}
