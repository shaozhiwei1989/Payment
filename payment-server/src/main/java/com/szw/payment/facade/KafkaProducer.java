package com.szw.payment.facade;

import com.szw.payment.common.Constants;
import com.szw.payment.common.utils.GsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.springframework.kafka.core.KafkaTemplate;

@Named("kafka_producer")
public class KafkaProducer<T> implements MessageProducer<T> {

	@Inject
	private KafkaTemplate<String, String> kafkaTemplate;


	@Override
	public void send(String topic, String tag, T t) {
		String json = GsonUtil.GSON.toJson(t);

		ProducerRecord<String, String> record = new ProducerRecord<>(topic, json);
		if (tag != null && !tag.trim().isEmpty()) {
			record.headers().add(Constants.Tag.KEY, tag.getBytes());
		}
		kafkaTemplate.send(record);
	}

}
