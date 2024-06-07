package com.szw.payment.facade;

public interface MessageProducer<T> {

	void send(String topic, String tag, T t);
}
