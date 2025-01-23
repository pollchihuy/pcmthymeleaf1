package com.example.pcmthymeleaf1.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ResponseHandler {

    public ResponseEntity<Object> handleResponse(String message,
                                                 HttpStatus status,
                                                 Object obj,
                                                 Object errorCode,
                                                 HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("status",status.value());
        map.put("data",obj==null?"":obj);
        map.put("timestamp",new Date());
        map.put("success",!status.isError());
        if(errorCode!=null) {
            map.put("error-code",errorCode);
            map.put("path",request.getContextPath());
        }
        return new ResponseEntity<>(map,status);
    }
}
