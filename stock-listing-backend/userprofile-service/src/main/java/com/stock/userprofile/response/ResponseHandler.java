package com.stock.userprofile.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("message", message);
		responseMap.put("status", status.value());
		responseMap.put("responseObj", responseObj);
		return new ResponseEntity<>(responseMap, status);
	}
}
