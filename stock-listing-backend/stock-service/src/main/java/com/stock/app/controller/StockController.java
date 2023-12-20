package com.stock.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.app.exception.ExternalServiceException;
import com.stock.app.exception.InvalidCredentialsException;
import com.stock.app.feign.AuthClient;
import com.stock.app.service.StockService;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1.0/stocks")
public class StockController {
	private final StockService stockService;
	private final AuthClient authClient;

	@Autowired
	public StockController(StockService stockService, AuthClient authClient) {
		this.stockService = stockService;
		this.authClient = authClient;
	}

	@GetMapping("/country/{country}")
	@Operation(summary = "listing stocks by countrys")
	public ResponseEntity<?> getStocksByCountry(@PathVariable String country,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws InvalidCredentialsException {
		try {
			Map<String, String> userInfo = (Map<String, String>) authClient.validateToken(token).getBody();
			if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
				return new ResponseEntity<>(stockService.getStocksByCountryName(country), HttpStatus.OK);
			} else {
				throw new InvalidCredentialsException("Access Denied");
			}
		} catch (FeignException e) {
			throw new ExternalServiceException(e.getMessage());
		}

	}
}
