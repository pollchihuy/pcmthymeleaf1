package com.example.pcmthymeleaf1.util;

import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class SetGlobalAttribute {

    public static void setAttribut(Model model, WebRequest webRequest){
        model.addAttribute("MENU_NAVBAR",webRequest.getAttribute("MENU_NAVBAR",1));
        model.addAttribute("USR_NAME",webRequest.getAttribute("USR_NAME",1));
    }
}
