package com.example.pcmthymeleaf1.util;

import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@Component
public class TokenExpired {

    @Autowired
    private AuthServices authServices;

    public void setAuthExpired(Model model, WebRequest webRequest){

        ValLoginDTO valLoginDTO = new ValLoginDTO();
        valLoginDTO.setUsername(webRequest.getAttribute("USR_NAME",1).toString());
        valLoginDTO.setPassword(webRequest.getAttribute("PASSWORD",1).toString());
        ResponseEntity<Object>  response = authServices.login(valLoginDTO);
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        List<Map<String,Object>> lt = (List<Map<String, Object>>) map.get("menu");
        String menuNavBar = new GenerateStringMenu().stringMenu(lt);
        String tokenJwt = map.get("token").toString();
        webRequest.setAttribute("JWT",tokenJwt,1);
        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
    }
}
