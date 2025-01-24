package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.GroupMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("group-menu")
public class GroupMenuController {

    @Autowired
    private GroupMenuService groupMenuService;


    @GetMapping
    public String findAll(Model model){

        ResponseEntity<Object> response = null;
        try{
            response = groupMenuService.findAll("Bearer ea2fcd8935893efdb6437483a852fd62d6905ae0e40a314368b802be1c80658f65284fdaa45ecebee35c35d9098aa7a6a60b344a27be785a99514e706f0d8274324bb3fe604c404fe3e48ceaf1e6219a5f2159990731c4acffce7416869811b0964bdfdf5473a61f070aa8bdee9ea285bad0dff60460bad78a090c95a46be24ffc55670ea4b5a66e56fc0512e4e9a62ecad91985d5a9fa8c94c1ed05504afd8485d1987762c73bef1e382a6344e7528d02c49cd8c2812f3549c0bb2493fc690df55d82ed10bfecdeaf147f73604eb98e0e6f1b7e8f9e828cabb99dc3899e7e19a6f9959151d735382064164a8bb370069cbac8d3676d63e1554589bf04187f386fc84b7df5af8bb47a2895bc09835ea9");
//            response = groupMenuService.findAll("");
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return "login";
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        model.addAttribute("usr", new ValLoginDTO());
        return "login";
    }
}
