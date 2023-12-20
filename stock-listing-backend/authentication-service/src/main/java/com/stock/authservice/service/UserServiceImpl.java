package com.stock.authservice.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stock.authservice.dto.LoginDto;
import com.stock.authservice.dto.ResetDto;
import com.stock.authservice.dto.SignupDto;
import com.stock.authservice.exception.InvalidInputException;
import com.stock.authservice.model.Role;
import com.stock.authservice.model.User;
import com.stock.authservice.repo.RoleRepository;
import com.stock.authservice.repo.UserRepository;
import com.stock.userprofile.model.UserProfile;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	Map<String, String> mapObj = new HashMap<>();

	@Override
	public ResponseEntity<?> addUser(SignupDto signupDto) {
		if (userRepository.existsByUsername(signupDto.getUsername())
				|| userRepository.existsByEmail(signupDto.getEmail())) {
			mapObj.put("msg", "Username or email is already exists!");
			return new ResponseEntity<>(mapObj, HttpStatus.BAD_REQUEST);
		}
		User user = new User(signupDto.getUsername(), signupDto.getEmail(), signupDto.getPassword(),
				signupDto.getSecurityQuestion(), signupDto.getSecurityAnswer());

		Set<String> strRoles = signupDto.getRoles();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName("ROLE_ADMIN")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		mapObj.put("msg", "User registered successfully");
		return new ResponseEntity<>(mapObj, HttpStatus.OK);
	}

	@Override
	public boolean loginUser(LoginDto loginRequest) {

		Optional<User> userObj = userRepository.findByUsername(loginRequest.getUsername());
		if (userObj.isPresent() && userObj.get().getPassword().equals(loginRequest.getPassword())) {
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<?> updatePassword(ResetDto resetDto) {

		if (!userRepository.existsByUsername(resetDto.getUsername())) {
			mapObj.put("msg", "Username doesn't exists!");
			return new ResponseEntity<>(mapObj, HttpStatus.BAD_REQUEST);
		}
		Optional<User> userdata = userRepository.findByUsername(resetDto.getUsername());
		if (resetDto.getSecQuestion().equalsIgnoreCase(userdata.get().getSecurityQuestion())
				&& resetDto.getSecAnswer().equalsIgnoreCase(userdata.get().getSecurityAnswer())) {
			userdata.get().setPassword(resetDto.getNewPassword());
			userRepository.save(userdata.get());
			mapObj.put("msg", "changed password successfully!");
			return new ResponseEntity<>(mapObj, HttpStatus.OK);
		}
		mapObj.put("msg", "could not update password(cause:sec ques not match)!");
		return new ResponseEntity<>(mapObj, HttpStatus.BAD_REQUEST);

	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new InvalidInputException("username is not present");
		}
		return user;
	}

	@Override
	public ResponseEntity<?> getAllUsers() {
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteUser(Long userId) {
		Optional<User> userData = userRepository.findById(userId);
		if (userData.isEmpty()) {
			mapObj.put("msg", "can not delete user!");
			return new ResponseEntity<>(mapObj, HttpStatus.BAD_REQUEST);
		}
		userRepository.deleteById(userId);
		mapObj.put("msg", "user " + userData.get().getUsername() + " deleted successfully!");
		return new ResponseEntity<>(mapObj, HttpStatus.OK);
	}

	//user registeration using kafka
	@Override
	public ResponseEntity<?> registerUser(UserProfile userProfileDto) {
		if (userRepository.existsByUsername(userProfileDto.getUsername())
				|| userRepository.existsByEmail(userProfileDto.getEmail())) {
			mapObj.put("msg", "Username or email is already exists!");
			return new ResponseEntity<>(mapObj, HttpStatus.BAD_REQUEST);
		}
		User user = new User(userProfileDto.getUsername(), userProfileDto.getEmail(), userProfileDto.getPassword(),
				userProfileDto.getSecurityQuestion(), userProfileDto.getSecurityAnswer());

		Set<String> strRoles = userProfileDto.getRoles();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName("ROLE_ADMIN")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		mapObj.put("msg", "User registered successfully");
		return new ResponseEntity<>(mapObj, HttpStatus.OK);
	}

}
