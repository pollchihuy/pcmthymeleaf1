package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.RespMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.httpclient.MenuService;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    private Map<String,Object> filterColumn=new HashMap<>();

    public MenuController() {
        filterColumn.put("nama","Nama Menu");
        filterColumn.put("path","Path Menu");
        filterColumn.put("group","Group");
    }

    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = menuService.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"menu");
        GlobalFunction.setGlobalFragment(model,webRequest);
        model.addAttribute("filterColumn",filterColumn);
        return ListPage.menuMainPage;
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    public String findAll(
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
            response = menuService.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFound(model,"menu",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.menuMainPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOK(model,"menu",webRequest,map,filterColumn);
        return ListPage.menuMainPage;
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
            response= menuService.downloadPDF("Bearer "+jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
//            GlobalFunction.statusNotFound(model,"group-menu",webRequest,filterColumn,e.getCause().getMessage());
//            return ListPage.menuMainPage;
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
            response= menuService.downloadExcel("Bearer "+jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
        }
        HttpHeaders headers = new HttpHeaders();
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
            response = menuService.findById("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("pesan", e.getCause().getMessage());
            return ListPage.menuMainPage;
        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
//        ObjectMapper objectMapper = new ObjectMapper();
//        RespMenuDTO respGroupMenuDTO = objectMapper.convertValue(mapData, RespMenuDTO.class);
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,RespMenuDTO.class));
        return ListPage.menuEditPage;
    }

    @GetMapping("/a")
    public String openModalAdd(Model model, WebRequest webRequest){
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        model.addAttribute("data",new RespMenuDTO());
        return ListPage.menuAddPage;
    }

    @PostMapping("/a")
    public String save(
            @ModelAttribute("data") @Valid ValMenuDTO valMenuDTO,
            BindingResult result,
            Model model, WebRequest webRequest){
        if(result.hasErrors()){
            model.addAttribute("data",valMenuDTO);
            return ListPage.menuAddPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = menuService.save("Bearer "+jwt,valMenuDTO);
        }catch (Exception e){
            model.addAttribute("data",new RespMenuDTO());
            result.addError(new ObjectError("globalError",e.getCause().getMessage()));
            return ListPage.menuAddPage;
        }
        model.addAttribute("pesan","Data Berhasil Disimpan");
        return ListPage.menuMainPage;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid ValMenuDTO valMenuDTO,
            BindingResult result,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        valMenuDTO.setId(id);//ketika gagal submit id nya harus diset , karena tidak ada di dalam body request
        /** ketentuan untuk relasi, kalau semisal object nya tidak null, akan tetapi id nya null maka akan menyebabkan internal server error
         *  jadi kita butuh validasi khusus untuk relasinya , semisal nilainya tidak di set di sisi web maka kita null kan object nya -- aturan JPA
         */
        if(valMenuDTO.getGroupMenu().getId()==null){
            valMenuDTO.setGroupMenu(null);
        }
        if(result.hasErrors()){

            model.addAttribute("data",valMenuDTO);
            return ListPage.menuEditPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = menuService.update("Bearer "+jwt,id,valMenuDTO);
        }catch (Exception e){
            System.out.println(e.getMessage());
            model.addAttribute("data",valMenuDTO);
            result.addError(new ObjectError("globalError",e.getCause().getMessage()));
            return ListPage.menuEditPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.menuMainPage;
    }

    @GetMapping("/d/{id}")
    public String delete(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try {
            response = menuService.delete("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Dihapus");
        return ListPage.menuMainPage;
    }
}