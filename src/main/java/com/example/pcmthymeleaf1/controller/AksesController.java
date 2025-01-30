package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.AksesService;
import com.example.pcmthymeleaf1.util.GlobalFunction;
import com.example.pcmthymeleaf1.util.ListPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("akses")
public class AksesController {


    @Autowired
    private AksesService aksesService;

    @GetMapping()
    public String findAll(Model model){

        ResponseEntity<Object> response = null;
        try{
            response = aksesService.findAll("Bearer ea2fcd8935893efdb6437483a852fd62d6905ae0e40a314368b802be1c80658f65284fdaa45ecebee35c35d9098aa7a6a60b344a27be785a99514e706f0d82748a97ea1a854ea373953fbb9784c7f02bca26a8c9464eb69eb8883cec0d8509786dd4fd6d31ebd6f98bd153ba574269b5270243722c6853816f99624dc305b756f338b391c56d96f996f646f69ba04648d2e491495c3f0860ec9d6fadce28599824d74c06978ae78fedf7d04cc742a766e3d45b91a10b4b507fd1fea092ef0beddbdeb170b90cb1c85323fc9f6bbd1a13c417963c5617fa4140bc46d215fd9d16911c8aab7375916d5ac7f416b45357b200cd21ae185fc08356020a2dd67c65c7d95c431a9cb63189a2ad650df5399519");
        }catch (Exception e){
            System.out.println(e.getCause().getMessage());
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"akses");
        return "akses/main";
    }

}
