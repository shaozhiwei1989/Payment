package com.szw.payment.producer;

import com.szw.payment.common.utils.GsonUtil;
import jakarta.inject.Named;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Named("rocketmq_producer")
public class RocketMQProducer<T> implements MessageProducer<T> {

	@Autowired(required = false)
	private RocketMQTemplate rocketMQTemplate;


	@Override
	public void send(String topic, String tag, T t) {
		StringBuilder builder = new StringBuilder(topic);
		if (tag != null && !tag.trim().isEmpty()) {
			builder.append(":").append(tag);
		}

		String json = GsonUtil.GSON.toJson(t);
		Message<String> message = MessageBuilder.withPayload(json).build();
		rocketMQTemplate.syncSend(builder.toString(), message);
	}

}
