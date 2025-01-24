package com.example.pcmthymeleaf1.httpclient;


import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "group-menu-services",url = "http://localhost:8080/group-menu")
public interface GroupMenuService {

    @GetMapping("")
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);
}
