package com.stock.whishlist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.whishlist.dto.WhishList;
import com.stock.whishlist.exception.ExternalServiceException;
import com.stock.whishlist.exception.InvalidCredentialsException;
import com.stock.whishlist.feign.AuthClient;
import com.stock.whishlist.service.WhishListService;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1.0/wishlist")
public class WhishListController {

	@Autowired
	WhishListService whishListService;

	@Autowired
	AuthClient authClient;

	//kafka
	@GetMapping("/getByUserId/{userId}")
	public ResponseEntity<?> getWhishlistByUserId(@PathVariable String userId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
		try {
			Map<String, String> userInfo = (Map<String, String>) authClient.validateToken(token).getBody();
			if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
				return new ResponseEntity<>(whishListService.getWhishlistByUserId(userId), HttpStatus.OK);
			} else {
				throw new InvalidCredentialsException("Access Denied");
			}
		} catch (FeignException e) {
			throw new ExternalServiceException(e.getMessage());
		}
	}

	@PostMapping("/addItem")
	public ResponseEntity<?> addStockToWhishlist(@RequestBody WhishList whishlist,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
		try {
			Map<String, String> userInfo = (Map<String, String>) authClient.validateToken(token).getBody();
			if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
				String userId = userInfo.keySet().iterator().next();
				System.out.println("userIdv " + userId);
				whishlist.setUserId(userId);
				return new ResponseEntity<>(whishListService.addStockToWhishlist(whishlist), HttpStatus.OK);
			} else {
				throw new InvalidCredentialsException("Access Denied");
			}
		} catch (FeignException e) {
			throw new ExternalServiceException(e.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteFromWhishList(@PathVariable Long id,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
		try {
			Map<String, String> userInfo = (Map<String, String>) authClient.validateToken(token).getBody();
			if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
				return new ResponseEntity<>(whishListService.deleteFromWhishList(id), HttpStatus.OK);
			} else {
				throw new InvalidCredentialsException("Access Denied");
			}
		} catch (FeignException e) {
			throw new ExternalServiceException(e.getMessage());
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateWhishList(@PathVariable Long id, @RequestBody WhishList whishlist,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
		try {
			Map<String, String> userInfo = (Map<String, String>) authClient.validateToken(token).getBody();
			if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
				return new ResponseEntity<>(whishListService.updateWhishList(id, whishlist), HttpStatus.OK);
			} else {
				throw new InvalidCredentialsException("Access Denied");
			}
		} catch (FeignException e) {
			throw new ExternalServiceException(e.getMessage());
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllStocksWhishlist(
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
		try {
			Map<String, String> userInfo = (Map<String, String>) authClient.validateToken(token).getBody();
			if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
				return new ResponseEntity<>(whishListService.getAllStocksWhishlist(), HttpStatus.OK);
			} else {
				throw new InvalidCredentialsException("Access Denied");
			}
		} catch (FeignException e) {
			throw new ExternalServiceException(e.getMessage());
		}
	}
}
