package com.example.pcmthymeleaf1.httpclient;


import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValRegisDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValVerifyRegisDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-services",url = "http://localhost:8080/auth")
public interface AuthServices {

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody ValLoginDTO valLoginDTO) ;

    @GetMapping("/coba")
    public ResponseEntity<Object> coba() ;


    @PostMapping("/regis")
    public ResponseEntity<Object> register(@RequestBody ValRegisDTO regisDTO);

    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegister(@RequestBody ValVerifyRegisDTO valVerifyRegisDTO);
}
