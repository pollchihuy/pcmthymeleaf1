package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.RespUserDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValUserDTO;
import com.example.pcmthymeleaf1.httpclient.UserService;
import com.example.pcmthymeleaf1.util.GlobalFunction;
import com.example.pcmthymeleaf1.util.ListPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = userService.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.setDataTable(model,map,"user");
        GlobalFunction.setGlobalFragment(model,webRequest);
        return ListPage.userMainPage;
    }

    @GetMapping("/e/{id}")
    public String openModalsEdit(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = userService.findById("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("pesan", e.getCause().getMessage());
            return ListPage.userMainPage;
        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
//        ObjectMapper objectMapper = new ObjectMapper();
//        RespUserDTO respGroupMenuDTO = objectMapper.convertValue(mapData, RespUserDTO.class);
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,RespUserDTO.class));
        return ListPage.userEditPage;
    }

    @GetMapping("/a")
    public String openModalAdd(Model model, WebRequest webRequest){
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        model.addAttribute("data",new RespUserDTO());
        return ListPage.userAddPage;
    }

    @PostMapping("/a")
    public String save(
            @ModelAttribute("data") @Valid ValUserDTO valUserDTO,
            BindingResult result,
            Model model, WebRequest webRequest){
        if(result.hasErrors()){
            model.addAttribute("data",valUserDTO);
            return ListPage.userAddPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = userService.save("Bearer "+jwt,valUserDTO);
        }catch (Exception e){
            model.addAttribute("data",new RespUserDTO());
            result.addError(new ObjectError("globalError",e.getCause().getMessage()));
            return ListPage.userAddPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.userMainPage;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid ValUserDTO valUserDTO,
            BindingResult result,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        valUserDTO.setId(id);//ketika gagal submit id nya harus diset , karena tidak ada di dalam body request
        /** ketentuan untuk relasi, kalau semisal object nya tidak null, akan tetapi id nya null maka akan menyebabkan internal server error
         *  jadi kita butuh validasi khusus untuk relasinya , semisal nilainya tidak di set di sisi web maka kita null kan object nya -- aturan JPA
         */
        if(valUserDTO.getAkses().getId()==null){
            valUserDTO.setAkses(null);
        }

        if(result.hasErrors()){
            valUserDTO.setId(id);
            model.addAttribute("data",valUserDTO);
            return ListPage.userEditPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = userService.update("Bearer "+jwt,id,valUserDTO);
        }catch (Exception e){
            System.out.println(e.getMessage());
            model.addAttribute("data",valUserDTO);
            result.addError(new ObjectError("globalError",e.getCause().getMessage()));
            return ListPage.userEditPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.userMainPage;
    }

    @GetMapping("/d/{id}")
    public String delete(Model model, @PathVariable(value = "id") Long id, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try {
            response = userService.delete("Bearer "+jwt,id);
        }catch (Exception e){
            model.addAttribute("usr",new ValLoginDTO());
            return ListPage.loginPage;
        }
        model.addAttribute("pesan","Data Berhasil Dihapus");
        return ListPage.userMainPage;
    }
}