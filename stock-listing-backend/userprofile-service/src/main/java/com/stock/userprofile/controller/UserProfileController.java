package com.stock.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.userprofile.dto.UserProfileDto;
import com.stock.userprofile.kafka.JsonKafkaProducer;
import com.stock.userprofile.model.UserProfile;
import com.stock.userprofile.service.UserProfileService;

@RestController
@RequestMapping("/api/v1.0/userProfile")
public class UserProfileController {
	
	private final UserProfileService usersProfileService;
	
	private final JsonKafkaProducer jsonKafkaProducer;

	@Autowired
	public UserProfileController(UserProfileService userProfileService,JsonKafkaProducer jsonKafkaProducer) {
		this.usersProfileService = userProfileService;
		this.jsonKafkaProducer = jsonKafkaProducer;
	}

	@GetMapping
	public ResponseEntity<Object> getAllUsers() {
		return new ResponseEntity<>(usersProfileService.getAllUsers(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getUserProfileById(@PathVariable long id) {
		return new ResponseEntity<>(usersProfileService.getUserProfileById(id), HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Object> saveUserProfile(@RequestBody UserProfile userProfile) {
		//sending registeration data to authentication server using kafka --comment me if kafka gives issues
	//	jsonKafkaProducer.sendMessage(userProfile);
		return new ResponseEntity<>(usersProfileService.saveUserProfile(userProfile), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateUserProfile(@RequestBody UserProfileDto userProfileDto, @PathVariable long id) {
		return new ResponseEntity<>(usersProfileService.updateUserProfile(userProfileDto, id), HttpStatus.OK);
	}
	
	@GetMapping("/getProfile/{username}")
	public ResponseEntity<Object> getUserProfileByName(@PathVariable String username) {
		return new ResponseEntity<>(usersProfileService.getUserByUsername(username), HttpStatus.OK);
	}
}
