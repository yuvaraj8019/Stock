package com.stock.authservice.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stock.authservice.dto.JwtResponse;
import com.stock.authservice.dto.LoginDto;
import com.stock.authservice.dto.ResetDto;
import com.stock.authservice.dto.SignupDto;
import com.stock.authservice.exception.InvalidInputException;
import com.stock.authservice.model.User;
import com.stock.authservice.security.JwtTokenUtil;
import com.stock.authservice.service.UserService;

@RestController
@RequestMapping("/api/v1.0/authentication")
public class AuthorizationController {

	@Autowired
	UserService userService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	//backup endpoint for user registeration in case kafka fails
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpRequest) {
		try {
			return userService.addUser(signUpRequest);
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDto loginRequest) throws InvalidInputException {
		try {
			String jwtToken = jwtTokenUtil.generatToken(loginRequest);
			Optional<User> userDetails = userService.getUserByUsername(loginRequest.getUsername());// fetch the user
			if (userDetails.isPresent()) {
				return ResponseEntity.ok(new JwtResponse(jwtToken, userDetails.get().getId(),
						userDetails.get().getUsername(), userDetails.get().getEmail(), userDetails.get().getRoles()));
			} else {
				throw new InvalidInputException("Invalid Credentials");
			}
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
		if (jwtTokenUtil.validateJwtToken(token)) {
			Map<String, String> userInfo = new HashMap<>();
			String authToken = token.substring(7);
			String username = jwtTokenUtil.getUserNameFromJwtToken(authToken);
			String role = jwtTokenUtil.getRoleFromToken(authToken);
			userInfo.put(username, role);
			return ResponseEntity.status(HttpStatus.OK).body(userInfo);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
	}

	@PatchMapping("/forgot")
	public ResponseEntity<?> forgotPassword(@RequestBody ResetDto resetDto) {
		return userService.updatePassword(resetDto);

	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token) {
		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("msg", "access denied");
		String authToken = token.substring(7);
		String role = jwtTokenUtil.getRoleFromToken(authToken);
		if (role.equals("ROLE_ADMIN")) {
			return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseObj);
		}
	}

	@DeleteMapping("/deleteUser")
	public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token,@RequestParam("userId") Long userId) {
		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("msg", "access denied");
		String authToken = token.substring(7);
		String role = jwtTokenUtil.getRoleFromToken(authToken);
		if (role.equals("ROLE_ADMIN")) {
			return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseObj);
		}
	}

}
