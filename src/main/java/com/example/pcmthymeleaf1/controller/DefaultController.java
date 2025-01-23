package com.example.pcmthymeleaf1.controller;

import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {


    @GetMapping("/")
    public String loginPage(Model model){
//        ValLoginDTO valLoginDTO = new ValLoginDTO();
//        valLoginDTO.setUsername("admin");
//        valLoginDTO.setPassword("zzzz");
        model.addAttribute("usr", new ValLoginDTO());
//        model.addAttribute("usr", valLoginDTO);

        return "login";
    }

    //localhost:8081/a
    @GetMapping("/a")
    public String pageContoh(){

        return "/contoh/x";
    }
}