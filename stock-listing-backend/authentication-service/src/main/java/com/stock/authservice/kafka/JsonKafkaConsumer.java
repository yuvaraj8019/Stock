package com.stock.authservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.stock.authservice.service.UserService;
import com.stock.userprofile.model.UserProfile;

@Service
public class JsonKafkaConsumer {
	
	@Autowired
	UserService userService;//for consuming data from kafka and register the user
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(UserProfile user){
        LOGGER.info(String.format("Json message recieved -> %s", user.toString()));
        userService.registerUser(user);//user registeration using kafka
    }
}