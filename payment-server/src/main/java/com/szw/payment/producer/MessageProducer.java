package com.szw.payment.producer;

public interface MessageProducer<T> {

	void send(String topic, String tag, T t);
}
