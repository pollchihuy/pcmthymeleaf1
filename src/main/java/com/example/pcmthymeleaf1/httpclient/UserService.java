package com.example.pcmthymeleaf1.httpclient;

import com.example.pcmthymeleaf1.dto.validasi.ValUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "user-services",url = "http://localhost:8080/user")
public interface UserService {

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
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,@RequestBody ValUserDTO valUserDTO);

    @PostMapping(value="/files/upload/{username}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestHeader("Authorization") String token,
                                             @PathVariable(value = "username") String username,
                                             @RequestPart(value = "file") MultipartFile file);

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id,
                                         @RequestBody ValUserDTO valUserDTO);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Long id);
}
