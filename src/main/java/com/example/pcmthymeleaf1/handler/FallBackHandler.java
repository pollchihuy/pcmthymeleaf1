package com.example.pcmthymeleaf1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FallBackHandler  implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        ResponseHandler message = null;
//        System.out.println("EXCEPTION "+response.body());
//        try (InputStream bodyIs = response.body().asInputStream()) {
//            ObjectMapper mapper = new ObjectMapper();
//            message = mapper.readValue(bodyIs, ResponseHandler.class);
//            System.out.println(message);
//        } catch (IOException e) {
//            return new Exception(e.getMessage());
//        }

        return null;
    }
}
