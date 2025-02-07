package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.RespMaster1DTO;
import com.example.pcmthymeleaf1.dto.validasi.ValMaster1DTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.Master1Service;
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
@RequestMapping("master1")
public class Master1Controller {

    @Autowired
    private Master1Service master1Service;
    private Map<String,Object> filterColumn=new HashMap<>();

    public Master1Controller() {
        filterColumn.put("nama","Nama Master 1");
        filterColumn.put("total","Total");
        filterColumn.put("jumlah","Jumlah");
        filterColumn.put("deskripsi","Deskripsi");
    }

    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = master1Service.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"master1");
        GlobalFunction.setGlobalFragment(model,webRequest);
        model.addAttribute("filterColumn",filterColumn);
        return ListPage.master1MainPage;
    }

    @GetMapping("/{idComp}/{descComp}/{sort}/{sortBy}/{page}")
    public String dataTable(Model model,
                            @PathVariable(value = "sort") String sort,
                            @PathVariable(value = "sortBy") String sortBy,//name
                            @PathVariable(value = "page") Integer page,
                            @RequestParam(value = "size") Integer size,
                            @RequestParam(value = "column") String column,
                            @RequestParam(value = "value") String value,
                            @PathVariable(value = "idComp") String idComp,
                            @PathVariable(value = "descComp") String descComp,
                WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        page = page!=0?(page-1):page;
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = master1Service.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFoundDataMaster(model,"master1",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.dataTableModals;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOKDataMaster(model,"master1",webRequest,map,filterColumn);
        model.addAttribute("idComp", idComp);
        model.addAttribute("descComp",descComp);
        return ListPage.dataTableModals;
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    public String findByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,//name
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            page = page!=0?(page-1):page;
            response = master1Service.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFound(model,"master1",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.master1MainPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOK(model,"master1",webRequest,map,filterColumn);
        return ListPage.master1MainPage;
    }

    @GetMapping("/e/{id}")
    public String openModalsEdit(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = master1Service.findById("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("pesan", e.getCause().getMessage());
            return ListPage.master1MainPage;
        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
//        ObjectMapper objectMapper = new ObjectMapper();
//        RespMaster1DTO respGroupMenuDTO = objectMapper.convertValue(mapData, RespMaster1DTO.class);
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,RespMaster1DTO.class));
        return ListPage.master1EditPage;
    }

    @GetMapping("/a")
    public String openModalAdd(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        model.addAttribute("data",new RespMaster1DTO());
        return ListPage.master1AddPage;
    }

    @PostMapping("/a")
    public String save(
            @ModelAttribute("data") @Valid ValMaster1DTO valMaster1DTO,
            BindingResult result,
            Model model, WebRequest webRequest){
        if(result.hasErrors()){
            model.addAttribute("data",valMaster1DTO);
            return ListPage.master1AddPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = master1Service.save("Bearer "+jwt,valMaster1DTO);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.master1MainPage;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid ValMaster1DTO valMaster1DTO,
            BindingResult result,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        valMaster1DTO.setId(id);

        if(result.hasErrors()){
            model.addAttribute("data",valMaster1DTO);
            return ListPage.master1EditPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = master1Service.update("Bearer "+jwt,id,valMaster1DTO);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.master1MainPage;
    }

    @GetMapping("/d/{id}")
    public String delete(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try {
            response = master1Service.delete("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Dihapus");
        return ListPage.master1MainPage;
    }
}