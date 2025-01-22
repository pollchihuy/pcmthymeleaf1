package com.example.pcmthymeleaf1.controller;

import ch.qos.logback.core.model.Model;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@ModelAttribute("usr")
                        @Valid ValLoginDTO valLoginDTO,
                        BindingResult result,
                        Model model,
                        WebRequest webRequest){
        if(result.hasErrors()){
            return "login";
        }
//        System.out.println("Password Sebelum Diubah : "+valLoginDTO.getPassword());
//        String strPassword= new String(Base64.decode(valLoginDTO.getPassword()));
//        valLoginDTO.setPassword(strPassword);
//        System.out.println("Password Sesudah Diubah : "+valLoginDTO.getPassword());
//        System.out.println(valLoginDTO.getPassword());


        return "home";
    }
}
