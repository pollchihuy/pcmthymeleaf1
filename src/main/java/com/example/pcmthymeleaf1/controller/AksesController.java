package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.RespAksesDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValAksesDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.AksesService;
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

import java.util.Map;

@Controller
@RequestMapping("akses")
public class AksesController {

    @Autowired
    private AksesService aksesService;


    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = aksesService.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"akses");
        GlobalFunction.setGlobalFragment(model,webRequest);
        return ListPage.aksesMainPage;
    }

    @GetMapping("/{idComp}/{descComp}")
    public String dataTable(Model model,
                            @PathVariable(value = "idComp") String idComp,
                            @PathVariable(value = "descComp") String descComp,
                WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = aksesService.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"akses");
        model.addAttribute("idComp", idComp);
        model.addAttribute("descComp",descComp);
        return ListPage.dataTableModals;
    }

    @GetMapping("/e/{id}")
    public String openModalsEdit(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = aksesService.findById("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("pesan", e.getCause().getMessage());
            return ListPage.aksesMainPage;
        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
//        ObjectMapper objectMapper = new ObjectMapper();
//        RespAksesDTO respGroupMenuDTO = objectMapper.convertValue(mapData, RespAksesDTO.class);
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,RespAksesDTO.class));
        return ListPage.aksesEditPage;
    }

    @GetMapping("/a")
    public String openModalAdd(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        model.addAttribute("data",new RespAksesDTO());
        return ListPage.aksesAddPage;
    }

    @PostMapping("/a")
    public String save(
            @ModelAttribute("data") @Valid ValAksesDTO valAksesDTO,
            BindingResult result,
            Model model, WebRequest webRequest){
        if(result.hasErrors()){
            model.addAttribute("data",valAksesDTO);
            return ListPage.aksesAddPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = aksesService.save("Bearer "+jwt,valAksesDTO);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.aksesMainPage;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid ValAksesDTO valAksesDTO,
            BindingResult result,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        valAksesDTO.setId(id);

        if(result.hasErrors()){
            model.addAttribute("data",valAksesDTO);
            return ListPage.aksesEditPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = aksesService.update("Bearer "+jwt,id,valAksesDTO);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.aksesMainPage;
    }

    @GetMapping("/d/{id}")
    public String delete(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try {
            response = aksesService.delete("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Dihapus");
        return ListPage.aksesMainPage;
    }
}