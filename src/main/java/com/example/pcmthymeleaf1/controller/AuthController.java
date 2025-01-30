package com.example.pcmthymeleaf1.controller;

import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValRegisDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValVerifyRegisDTO;
import com.example.pcmthymeleaf1.httpclient.AuthServices;
import com.example.pcmthymeleaf1.util.GenerateStringMenu;
import com.example.pcmthymeleaf1.util.ListPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.RequestContextFilter;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;
    @Autowired
    private RequestContextFilter requestContextFilter;

    @PostMapping("/login")
    public String login(@ModelAttribute("usr")
                        @Valid ValLoginDTO valLoginDTO,
                        BindingResult result,
                        Model model,
                        WebRequest webRequest){
        if(result.hasErrors()){
            return ListPage.loginPage;
        }

        valLoginDTO.setPassword(new String(Base64.decode(valLoginDTO.getPassword())));
        ResponseEntity<Object> response = null;
        String menuNavBar = "";
        String tokenJwt = "";
        try{
            response = authServices.login(valLoginDTO);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            List<Map<String,Object>> lt = (List<Map<String, Object>>) map.get("menu");
            menuNavBar = new GenerateStringMenu().stringMenu(lt);
            tokenJwt = map.get("token").toString();
        }catch (Exception e){
//            if(e.getMessage().toLowerCase().contains("expired")){
//
//            }
            model.addAttribute("usr",new ValLoginDTO());
            result.addError(new ObjectError("globalError",e.getCause().getMessage()));
            return ListPage.loginPage;
        }
        /** untuk di set di table session */
        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
        webRequest.setAttribute("JWT",tokenJwt,1);
        webRequest.setAttribute("USR_NAME",valLoginDTO.getUsername(),1);
        webRequest.setAttribute("PASSWORD",valLoginDTO.getPassword(),1);

        /** untuk di set di page home setelah login*/
        model.addAttribute("MENU_NAVBAR",menuNavBar);
        model.addAttribute("USR_NAME",valLoginDTO.getUsername());

        return ListPage.homePage;
    }

    @GetMapping("/regis")
    public String regis(Model model, WebRequest webRequest){

        model.addAttribute("regis", new ValRegisDTO());
        return ListPage.regisPage;

    }

    @PostMapping("/regis")
    public String regis(
            @ModelAttribute("regis") @Valid ValRegisDTO valRegisDTO,
            BindingResult result,
            Model model,
            WebRequest webRequest){
        ResponseEntity<Object> response = null;
        if(result.hasErrors()){
            model.addAttribute("regis", valRegisDTO);
            return ListPage.regisPage;
        }

        try{
            response = authServices.register(valRegisDTO);
        }catch (Exception e){
            return "redirect:"+ListPage.defaultPage;
        }
        /** kalau registrasi sudah berhasil , akan lanjut ke halaman verifikasi otp */
        model.addAttribute("mail",valRegisDTO.getEmail());
        model.addAttribute("pesan","Kode verifikasi sudah terkirim ke email anda");

        return ListPage.otpRegisPage;
    }

    @GetMapping("/home")
    public String home(Model model , WebRequest webRequest) {
        model.addAttribute("MENU_NAVBAR",webRequest.getAttribute("MENU_NAVBAR",1));
        model.addAttribute("USR_NAME",webRequest.getAttribute("USR_NAME",1));
        return ListPage.homePage;
    }

    @GetMapping("/test-verify-regis")
    public String testVerifyRegis(
            Model model , WebRequest webRequest) {

        model.addAttribute("mail","poll.exact@gmail.com");
        model.addAttribute("pesan","Kode verifikasi sudah terkirim ke email anda");
        return ListPage.otpRegisPage;
    }

    @GetMapping("/verify-regis")
    public String verifyRegis(
        Model model ,
        @RequestParam(value = "email") String email,
        @RequestParam(value = "otp") String otp,
         WebRequest webRequest) {

        if(otp.length()!=6){
            model.addAttribute("err","Format OTP Harus 6 Angka");
            model.addAttribute("mail",email);
            model.addAttribute("pesan","Kode verifikasi sudah terkirim ke email anda");
            return ListPage.otpRegisPage;
        }
        ValVerifyRegisDTO valVerifyRegisDTO = new ValVerifyRegisDTO();
        valVerifyRegisDTO.setEmail(email);
        valVerifyRegisDTO.setOtp(otp);
        ResponseEntity<Object> response = null;
        try{
            response = authServices.verifyRegister(valVerifyRegisDTO);
        }catch (Exception e){
            model.addAttribute("err",e.getCause().getMessage());
            model.addAttribute("mail",email);
            model.addAttribute("pesan","Kode verifikasi sudah terkirim ke email anda");
            return ListPage.otpRegisPage;
        }

        model.addAttribute("usr",new ValLoginDTO());
        model.addAttribute("reg","Registrasi Berhasil");
        return ListPage.loginPage;
    }

    @GetMapping("/success")
    public String successRegis(
            Model model , WebRequest webRequest) {
        model.addAttribute("usr",new ValLoginDTO());
        model.addAttribute("reg","Registrasi Berhasil");
        return ListPage.loginPage;
    }

    /** semisal error pada saat di main page , akan selalu di redirect ke url ini */
    @GetMapping("/relogin")
    public String relogin(
            Model model , WebRequest webRequest) {
        model.addAttribute("usr",new ValLoginDTO());
        model.addAttribute("reg","Lakukan Login Kembali");
        return ListPage.loginPage;
    }

    @GetMapping("/resend-otp-regis")
    public String resendRegisOtp(
            @RequestParam(value = "email") String email,
            Model model , WebRequest webRequest) {
        model.addAttribute("otpregis",new ValVerifyRegisDTO());
        model.addAttribute("mail",email);
        model.addAttribute("pesan","Sudah mengirim ulang kode verifikasi");
        return ListPage.otpRegisPage;
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:"+ListPage.defaultPage;
    }
}