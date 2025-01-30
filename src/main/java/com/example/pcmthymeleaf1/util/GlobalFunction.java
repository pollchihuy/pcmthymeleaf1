package com.example.pcmthymeleaf1.util;

import com.example.pcmthymeleaf1.dto.response.RespGroupMenuDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalFunction {

    public static String camelToStandar(String str)
    {
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        sb.append(Character.toLowerCase(c));
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(' ').append(Character.toLowerCase(ch));
            }
            else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    public static void setDataTable(Model model, Map<String,Object> map,String path){
        Map<String,Object> data = (Map<String, Object>) map.get("data");
        List<Map<String,Object>> listContent = (List<Map<String, Object>>) data.get("content");//ini sudah jadi data, maka yang kita butuh adalah column dari informasi ini
        List<String> listKolom = new ArrayList<>();
        List<String> listHelper = new ArrayList<>();
        Map<String,Object> columnHeader = (Map<String, Object>) listContent.get(0);
        //key yang nantinya akan menjadi column sudah di mapping ke dalam object ltHeader
        String keyVal = "";
        for(Map.Entry<String,Object> entry : columnHeader.entrySet()){
            keyVal = entry.getKey();
            listHelper.add(keyVal);
            listKolom.add(GlobalFunction.camelToStandar(keyVal).toUpperCase());
        }
        model.addAttribute("listKolom",listKolom);
        model.addAttribute("listContent",listContent);
        model.addAttribute("listHelper",listHelper);
        model.addAttribute("pathServer",path);
    }

    public static void setGlobalFragment(Model model , WebRequest webRequest){
        model.addAttribute("USR_NAME",webRequest.getAttribute("USR_NAME",1));
        model.addAttribute("MENU_NAVBAR",webRequest.getAttribute("MENU_NAVBAR",1));
    }

    public static String tokenCheck(Model model,WebRequest request){
        Object tokenJwt = request.getAttribute("JWT",1);
        if(tokenJwt == null){
            model.addAttribute("authProblem","Belum melakukan Login");
            model.addAttribute("usr", new ValLoginDTO());
            return ListPage.loginPage;
        }
        return tokenJwt.toString();
    }

    public static Object convertMapToPojo(Map<String,Object> map , Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        Object object1 = objectMapper.convertValue(map.get("data"),Object.class);
        return object1;

    }

}
