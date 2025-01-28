package com.example.pcmthymeleaf1.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "group-menu-services",url = "http://localhost:8080/group-menu")
public interface GroupMenuService {

    @GetMapping("")
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @PostMapping("")
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token);
}
