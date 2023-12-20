package com.stock.authservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "REGISTRATIONSERVICE")
public interface RegistrationClient {

    @GetMapping("/api/v1/userprofile/getUser")
    ResponseEntity<?> getUserByUserNameAndPassword(@RequestParam String userName, @RequestParam String password);
}