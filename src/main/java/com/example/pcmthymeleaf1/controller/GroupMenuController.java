package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.RespGroupMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValGroupMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.GroupMenuService;
import com.example.pcmthymeleaf1.util.GlobalFunction;
import com.example.pcmthymeleaf1.util.ListPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("group-menu")
public class GroupMenuController {

    @Autowired
    private GroupMenuService groupMenuService;
    private Map<String,Object> filterColumn=new HashMap<>();

    public GroupMenuController() {
        filterColumn.put("nama","Nama Group");
    }

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
//        GlobalFunction.setDataTable(model,map,"group-menu");
        GlobalFunction.statusOK(model,"group-menu",webRequest,map,filterColumn);
//        model.addAttribute("columnName", "id");
//        model.addAttribute("value", "5");
        return ListPage.groupMenuMainPage;
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
            response = groupMenuService.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFoundDataMaster(model,"group-menu",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.dataTableModals;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOKDataMaster(model,"group-menu",webRequest,map,filterColumn);
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
            response = groupMenuService.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFound(model,"group-menu",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.groupMenuMainPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOK(model,"group-menu",webRequest,map,filterColumn);
        return ListPage.groupMenuMainPage;
    }

    @GetMapping("/pdf")
    public ResponseEntity<Resource> pdf(
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            Model model, WebRequest webRequest, HttpServletResponse webResponse){
        Response response = null;
        ByteArrayResource resource =null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        String fileName = "";
        try{
            response= groupMenuService.downloadPDF("Bearer "+jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition",fileName);
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
    }

    @GetMapping("/excel")
    public ResponseEntity<Resource> excel(
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            Model model, WebRequest webRequest, HttpServletResponse webResponse){
        Response response = null;
        ByteArrayResource resource =null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        String fileName = "";
        try{
            response= groupMenuService.downloadExcel("Bearer "+jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
        }
        HttpHeaders headers = new HttpHeaders();// .xlxs]
        headers.set("Content-Disposition",fileName.substring(0,fileName.length()-1));
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
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
        valGroupMenuDTO.setId(id);

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