package com.szw.payment.test;

import java.time.LocalDateTime;

import com.szw.payment.bootstrap.Application;
import com.szw.payment.common.Constants;
import com.szw.payment.common.model.PayOrderMessage;
import com.szw.payment.facade.MessageProducer;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class MessageTest {

	@Inject
	@Named("kafka_producer")
	private MessageProducer<PayOrderMessage> messageProducer;

	@Value("${mq-topic.pay}")
	private String topic;


	@Test
	public void test1() {
		PayOrderMessage message = new PayOrderMessage();
		message.setTransactionId("TransactionId");
		message.setChannel("wx_app");
		message.setPayDoneTime(LocalDateTime.now());
		message.setPrePayId("prePayId");
		messageProducer.send(topic, Constants.Tag.ERROR, message);
	}

}
