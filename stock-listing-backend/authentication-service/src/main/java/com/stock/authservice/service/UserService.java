package com.stock.authservice.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.stock.authservice.dto.LoginDto;
import com.stock.authservice.dto.ResetDto;
import com.stock.authservice.dto.SignupDto;
import com.stock.authservice.model.User;
import com.stock.userprofile.model.UserProfile;

public interface UserService {

	public ResponseEntity<?> addUser(SignupDto signupDto);
	
	//kafka service
	public ResponseEntity<?> registerUser(UserProfile signupDto);

	// this will work like a authentication manager for validating user
	public boolean loginUser(LoginDto loginDto);

	public ResponseEntity<?> getAllUsers();

	public Optional<User> getUserByUsername(String username);

	public ResponseEntity<?> updatePassword(ResetDto resetDto);

	public ResponseEntity<?> deleteUser(Long userId);

}
