package com.example.pcmthymeleaf1.controller;

import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValRegisDTO;
import com.example.pcmthymeleaf1.httpclient.AuthServices;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;

    @PostMapping("/login")
    public String login(@ModelAttribute("usr")
                        @Valid ValLoginDTO valLoginDTO,
                        BindingResult result,
                        Model model,
                        WebRequest webRequest){
        if(result.hasErrors()){
            return "login";
        }

        valLoginDTO.setPassword(new String(Base64.decode(valLoginDTO.getPassword())));
        ResponseEntity<Object> response = null;
        try{
            response = authServices.login(valLoginDTO);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            List<Map<String,Object>> lt = (List<Map<String, Object>>) map.get("menu");
            for(Map<String,Object> m : lt){
                System.out.println("Group Menu : "+ m.get("group"));
                System.out.println("[");
                List<Map<String,Object>> f = (List<Map<String, Object>>) m.get("subMenu");

                for(Map<String,Object> f1 : f){

                    System.out.println("Nama Menu : "+f1.get("nama"));
                    System.out.println("Path Menu : "+f1.get("path"));
                }
                System.out.println("]");
                System.out.println("======================================");
            }

        }catch (Exception e){
            result.addError(new ObjectError("globalError",e.getCause().getMessage()));
            return "login";
        }

//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//        if(response.getStatusCode().value() == 200){
//
//        }else {
//            return "login";
//        }
        return "home";
    }

//    @GetMapping("/coba")
//    public String coba(
//                        Model model,
//                        WebRequest webRequest){
//
//        ResponseEntity<Object> response = authServices.coba();
//
//        if(response.getStatusCode().value() == 200){
//            System.out.println(response.getBody());
//            Map<String,Object> map = (Map<String, Object>) response.getBody();
//
//            List<Map<String,Object>> lt = (List<Map<String, Object>>) map.get("list");
//            for(Map<String,Object> map2 : lt){
//                System.out.println(map2.get("username"));
//                System.out.println(map2.get("password"));
//            }
//            System.out.println("Info "+map.get("info"));
////            for (Map.Entry<String, Object> entry : map.entrySet()) {
////                System.out.println(entry.getKey() + ": " + entry.getValue());
////                lt = (List<Map<String, Object>>) entry.getValue();
////                for(Map<String,Object> l : lt){
////                    System.out.println(l.get("username"));
////                    System.out.println(l.get("password"));
////                }
////            }
////            RespLoginDTO respLoginDTO = (RespLoginDTO) response.getBody();
////            System.out.println(respLoginDTO.getMenu());
////            System.out.println(respLoginDTO.getToken());
////            System.out.println(response.getHeaders());
//        }else {
//            return "redirect:/";
//        }
//
//        return "redirect:/";
//    }

    @GetMapping("/regis")
    public String regis(Model model, WebRequest webRequest){
        model.addAttribute("regis", new ValRegisDTO());
        return "register2";

    }

    @PostMapping("/regis")
    public String regis(
            @ModelAttribute("regis") @Valid ValRegisDTO valRegisDTO,
            BindingResult result,
            Model model,
            WebRequest webRequest){
//        model.addAttribute("regis", valRegisDTO);
        if(result.hasErrors()){
            return "register2";
        }
        ResponseEntity<Object> response = authServices.register(valRegisDTO);
        System.out.println(response.getBody());
        if(response.getStatusCode().value() == 200){
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }

        return "home";

    }


}
