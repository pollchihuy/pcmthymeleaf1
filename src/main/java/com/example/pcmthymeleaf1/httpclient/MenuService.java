package com.example.pcmthymeleaf1.httpclient;

import com.example.pcmthymeleaf1.dto.validasi.ValGroupMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValMenuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "menu-services",url = "http://localhost:8080/menu")
public interface MenuService {

    @GetMapping("")
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id);

    @PostMapping("")
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,@RequestBody ValMenuDTO valMenuDTO);

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id,
                                         @RequestBody ValMenuDTO valMenuDTO);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id);
}
