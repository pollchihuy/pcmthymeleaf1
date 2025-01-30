package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.RespGroupMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValGroupMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.GroupMenuService;
import com.example.pcmthymeleaf1.util.GlobalFunction;
import com.example.pcmthymeleaf1.util.ListPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("group-menu")
public class GroupMenuController {

    @Autowired
    private GroupMenuService groupMenuService;


    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = groupMenuService.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"group-menu");
        GlobalFunction.setGlobalFragment(model,webRequest);
        return ListPage.groupMenuMainPage;
    }

    @GetMapping("/e/{id}")
    public String openModalsEdit(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = groupMenuService.findById("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("pesan", e.getCause().getMessage());
            return ListPage.groupMenuMainPage;
        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
//        ObjectMapper objectMapper = new ObjectMapper();
//        RespGroupMenuDTO respGroupMenuDTO = objectMapper.convertValue(mapData, RespGroupMenuDTO.class);
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,RespGroupMenuDTO.class));
        return ListPage.groupMenuEditPage;
    }

    @GetMapping("/a")
    public String openModalAdd(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        model.addAttribute("data",new RespGroupMenuDTO());
        return ListPage.groupMenuAddPage;
    }

    @PostMapping("/a")
    public String save(
            @ModelAttribute("data") @Valid ValGroupMenuDTO valGroupMenuDTO,
            BindingResult result,
            Model model, WebRequest webRequest){
        if(result.hasErrors()){
            model.addAttribute("data",valGroupMenuDTO);
            return ListPage.groupMenuAddPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = groupMenuService.save("Bearer "+jwt,valGroupMenuDTO);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.groupMenuMainPage;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid ValGroupMenuDTO valGroupMenuDTO,
            BindingResult result,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){

        if(result.hasErrors()){
            model.addAttribute("data",valGroupMenuDTO);
            return ListPage.groupMenuEditPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = groupMenuService.update("Bearer "+jwt,id,valGroupMenuDTO);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.groupMenuMainPage;
    }

    @GetMapping("/d/{id}")
    public String delete(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try {
            response = groupMenuService.delete("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Dihapus");
        return ListPage.groupMenuMainPage;
    }
}