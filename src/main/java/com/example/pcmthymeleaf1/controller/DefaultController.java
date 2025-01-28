package com.example.pcmthymeleaf1.controller;

import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.util.ListPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {


    @GetMapping("/")
    public String loginPage(Model model){
        model.addAttribute("usr", new ValLoginDTO());
        return ListPage.loginPage;
    }

    //localhost:8081/a
    @GetMapping("/a")
    public String pageContoh(){

        return "/contoh/x";
    }
}