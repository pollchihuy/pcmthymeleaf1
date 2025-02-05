package com.example.pcmthymeleaf1.httpclient;

import com.example.pcmthymeleaf1.dto.validasi.ValGroupMenuDTO;
import com.example.pcmthymeleaf1.model.GroupMenu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "group-menu-services",url = "http://localhost:8080/group-menu")
public interface GroupMenuService {

    @GetMapping("")
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @GetMapping("/{sort}/{sortBy}/{page}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,//name
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value);

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id);

    @PostMapping("")
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,@RequestBody ValGroupMenuDTO valGroupMenuDTO);

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id,
                                         @RequestBody ValGroupMenuDTO valGroupMenuDTO);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id);
}
