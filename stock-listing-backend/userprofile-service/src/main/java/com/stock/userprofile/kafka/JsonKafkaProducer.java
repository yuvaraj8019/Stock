package com.stock.userprofile.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.stock.userprofile.model.UserProfile;

@Service
public class JsonKafkaProducer {

	@Value("${spring.kafka.topic-json.name}")
	private String topicJsonName;

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaProducer.class);

	private KafkaTemplate<String, UserProfile> kafkaTemplate;

	public JsonKafkaProducer(KafkaTemplate<String, UserProfile> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(UserProfile data) {

		LOGGER.info(String.format("Message sent -> %s", data.toString()));

		Message<UserProfile> message = MessageBuilder.withPayload(data).setHeader(KafkaHeaders.TOPIC, topicJsonName)
				.build();

		kafkaTemplate.send(message);
	}
}