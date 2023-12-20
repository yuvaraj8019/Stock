package com.stock.whishlist.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "REGISTRATIONSERVICE",url="http://localhost:9096/api/v1.0/authentication")
public interface AuthClient {
	
	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token);
}
