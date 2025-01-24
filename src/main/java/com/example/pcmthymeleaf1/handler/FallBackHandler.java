package com.example.pcmthymeleaf1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FallBackHandler  implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        ExceptionMessage message = null;
        JwtAuthenticationEntryPoint authMessage = null;
        int status = response.status();
        Exception e = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            switch (status){
                case 401: authMessage = mapper.readValue(bodyIs, JwtAuthenticationEntryPoint.class);
                default:message = mapper.readValue(bodyIs, ExceptionMessage.class);
            }
        } catch (IOException ei) {
//            return new Exception(ei.getMessage());
            return new Exception("Internal Server Error");
        }

        switch (status){
            case 401: e = new Exception(authMessage.getError());break;
            default: e = new Exception(message.getMessage()+"-"+message.getErrorCode());
        }
        return e;
    }
}
