package com.example.pcmthymeleaf1.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "akses-services",url = "http://localhost:8080/akses")
public interface AksesService {

    @GetMapping("")
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);
}
