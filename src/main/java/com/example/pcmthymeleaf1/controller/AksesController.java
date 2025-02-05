package com.example.pcmthymeleaf1.controller;


import com.example.pcmthymeleaf1.dto.response.*;
import com.example.pcmthymeleaf1.dto.validasi.ValAksesDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValLoginDTO;
import com.example.pcmthymeleaf1.dto.validasi.ValMenuDTO;
import com.example.pcmthymeleaf1.httpclient.AksesService;
import com.example.pcmthymeleaf1.httpclient.MenuService;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("akses")
public class AksesController {

    @Autowired
    private AksesService aksesService;

    @Autowired
    private MenuService menuService;


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
        ResponseEntity<Object> responseMenu = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = aksesService.findById("Bearer "+jwt,id);
            responseMenu = menuService.findAll("Bearer "+jwt);
        }catch (Exception e){
            model.addAttribute("pesan", e.getCause().getMessage());
            return ListPage.aksesMainPage;
        }

        setDataMenuToEdit(response,responseMenu,model,webRequest);
        return ListPage.aksesEditPage;
    }

    @GetMapping("/a")
    public String openModalAdd(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        try{
            response = menuService.findAll("Bearer "+jwt);
        }catch (Exception e){

        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
        List<Map<String,Object>> ltMenu = (List<Map<String, Object>>) mapData.get("content");
        int ltMenuSize = ltMenu.size();
        Long[] data1 = new Long[ltMenuSize];
        String[] data2 = new String[ltMenuSize];
        int i=0;
        for (Map<String,Object> map1 : ltMenu) {
            data1[i] = Long.parseLong(map1.get("id").toString());
            data2[i] = (String) map1.get("nama");
            i++;
        }
        webRequest.setAttribute("data1",data1,1);
        webRequest.setAttribute("data2",data2,1);

        model.addAttribute("data",new RespAksesDTO());
        model.addAttribute("listMenu",ltMenu);
        return ListPage.aksesAddPage;
    }

    @PostMapping("/a")
    public String save(
            @ModelAttribute("data") @Valid SelectAksesDTO selectAksesDTO,
            BindingResult result,Model model, WebRequest webRequest
            ){
        if(result.hasErrors()){
            model.addAttribute("data",selectAksesDTO);
            setDataTempAdd(model,webRequest);
            return ListPage.aksesAddPage;
        }
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }

        try{
            response = aksesService.save("Bearer "+jwt,convertToValAksesDTO(selectAksesDTO));
        }catch (Exception e){
            model.addAttribute("data",selectAksesDTO);
            setDataTempAdd(model,webRequest);
            model.addAttribute("pesan",e.getMessage()==null?e.getCause().getMessage():e.getMessage());
            return ListPage.aksesAddPage;
        }
        model.addAttribute("pesan","Data Berhasil Disimpan");
        return ListPage.aksesMainPage;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data")@Valid SelectAksesDTO selectAksesDTO,
            BindingResult result,
            Model model,
            WebRequest webRequest,
            @PathVariable(value = "id") Long id
            ){

        selectAksesDTO.setId(id);
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        /** Jwt harus selalu di awal sebelum melakukan request data ke REST API */
        if(jwt.equals(ListPage.loginPage)){
            return jwt;
        }
        if(result.hasErrors()){
            model.addAttribute("data",selectAksesDTO);
            setDataTempEdit(model,webRequest);
            return ListPage.aksesEditPage;
        }

        try{
            response = aksesService.update("Bearer "+jwt,id,convertToValAksesDTO(selectAksesDTO));
        }catch (Exception e){
            model.addAttribute("data",selectAksesDTO);
            model.addAttribute("pesan",e.getMessage()==null?e.getCause().getMessage():e.getMessage());
            setDataTempEdit(model,webRequest);
            return ListPage.aksesEditPage;
        }
        model.addAttribute("pesan","Data Berhasil Diubah");
        return ListPage.aksesMainPage;
    }

    private ValAksesDTO convertToValAksesDTO(SelectAksesDTO selectAksesDTO){
        ValAksesDTO valAksesDTO = new ValAksesDTO();
        valAksesDTO.setId(selectAksesDTO.getId());
        valAksesDTO.setNama(selectAksesDTO.getNama());
        List<ValMenuDTO> valMenuDTOList = new ArrayList<>();
        ValMenuDTO valMenuDTO = new ValMenuDTO();
        for (String s:
        selectAksesDTO.getLtMenu()) {
            valMenuDTO = new ValMenuDTO();
            valMenuDTO.setId(Long.parseLong(s));
            valMenuDTOList.add(valMenuDTO);
        }
        valAksesDTO.setLtMenu(valMenuDTOList);
        return valAksesDTO;
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

    /** fungsi untuk mencocokkan data menu dengan data yang sudah ada di table di database */
    private void setDataMenuToEdit(ResponseEntity<Object> response,ResponseEntity<Object> responseMenu, Model model,WebRequest webRequest){
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapMenu = (Map<String, Object>) responseMenu.getBody();

        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
        Map<String,Object> mapDataMenu = (Map<String, Object>) mapMenu.get("data");

        List<Map<String,Object>> ltMenuAkses = (List<Map<String, Object>>) mapData.get("ltMenu");
        List<Map<String,Object>> listOfMenu = (List<Map<String, Object>>) mapDataMenu.get("content");
        List<SelectMenuDTO> listAllMenu = getAllMenu(listOfMenu);//harus di serialize di thymeleaf
        int listAllMenuSize = listAllMenu.size();
        int listMenuAksesSize = ltMenuAkses.size();
        List<SelectMenuDTO> selectedMenuDTO = new ArrayList<>();
        String data1[] = new String[listAllMenuSize];//menampung data id dari all menu
        String data2[] = new String[listAllMenuSize];//menampung data nama dari all menu
        String data3[] = new String[listMenuAksesSize];//menampung data id dari menu akses
        String data4[] = new String[listMenuAksesSize];//menampung data nama dari menu akses

        int i=0;
        int j=0;
        for (SelectMenuDTO menu : listAllMenu) {
            data1[i]=String.valueOf(menu.getId());
            data2[i]=menu.getNama();
            for(Map<String,Object> m:ltMenuAkses){
                if(menu.getId()==Long.parseLong(m.get("id").toString())){
                    data3[j]=String.valueOf(menu.getId());
                    data4[j]=menu.getNama();
                    j++;
                    selectedMenuDTO.add(menu);
                    break;
                }
            }
            i++;
        }
        Set<Long> menuSelected = selectedMenuDTO.stream().map(SelectMenuDTO::getId).collect(Collectors.toSet());
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,RespAksesDTO.class));
        model.addAttribute("listMenu",listAllMenu);
        model.addAttribute("menuSelected", menuSelected);
        webRequest.setAttribute("data1",data1,1);
        webRequest.setAttribute("data2",data2,1);
        webRequest.setAttribute("data3",data3,1);
        webRequest.setAttribute("data4",data4,1);
    }

    /** fungsi untuk mengambil data web yang sudah di set sebelumnya di function setDataMenuToEdit , agar tidak menghubungi server lagi meminta data yang sama */
    private void setDataTempEdit(Model model , WebRequest webRequest){
        String data1[] = (String[]) webRequest.getAttribute("data1",1);//menampung data id dari all menu
        String data2[] = (String[]) webRequest.getAttribute("data2",1);//menampung data nama dari all menu
        String data3[] = (String[]) webRequest.getAttribute("data3",1);//menampung data id dari menu akses
        String data4[] = (String[]) webRequest.getAttribute("data4",1);//menampung data nama dari menu akses
        List<SelectMenuDTO> selectedMenuDTO = new ArrayList<>();
        List<SelectMenuDTO> listAllMenu = new ArrayList<>();
        SelectMenuDTO selectMenuDTO = null;
        for(int i=0;i<data1.length;i++){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(Long.parseLong(data1[i]));
            selectMenuDTO.setNama(data2[i]);
            listAllMenu.add(selectMenuDTO);
        }

        for(int i=0;i<data3.length;i++){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(Long.parseLong(data3[i]));
            selectMenuDTO.setNama(data4[i]);
            selectedMenuDTO.add(selectMenuDTO);
        }
        Set<Long> menuSelected = selectedMenuDTO.stream().map(SelectMenuDTO::getId).collect(Collectors.toSet());
        model.addAttribute("listMenu",listAllMenu);
        model.addAttribute("menuSelected", menuSelected);
    }

    /** fungsi untuk mengambil data web yang sudah di set sebelumnya di function openModalAdd , agar tidak menghubungi server lagi meminta data menu yang sama */
    private void setDataTempAdd(Model model , WebRequest webRequest){
        Long data1[] = (Long[]) webRequest.getAttribute("data1",1);//menampung data id dari all menu
        String data2[] = (String[]) webRequest.getAttribute("data2",1);//menampung data nama dari all menu
        List<SelectMenuDTO> listAllMenu = new ArrayList<>();
        SelectMenuDTO selectMenuDTO = null;
        for(int i=0;i<data1.length;i++){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(data1[i]);
            selectMenuDTO.setNama(data2[i]);
            listAllMenu.add(selectMenuDTO);
        }
        model.addAttribute("listMenu",listAllMenu);
    }

    public void setDataMenuToEdit(ResponseEntity<Object> response,ResponseEntity<Object> responseMenu, Model model){
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapMenu = (Map<String, Object>) responseMenu.getBody();

        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
        Map<String,Object> mapDataMenu = (Map<String, Object>) mapMenu.get("data");

//        SelectAksesDTO aksesDTO = new ObjectMapper().convertValue(mapData,SelectAksesDTO.class);
        List<Map<String,Object>> ltMenuAkses = (List<Map<String, Object>>) mapData.get("ltMenu");
//        List<SelectMenuDTO> ltMenuAkses = aksesDTO.getLtMenu();
        List<Map<String,Object>> listOfMenu = (List<Map<String, Object>>) mapDataMenu.get("content");
        List<SelectMenuDTO> listAllMenu = getAllMenu(listOfMenu);//harus di serialize di thymeleaf
        List<SelectMenuDTO> selectedMenuDTO = new ArrayList<>();
        for (SelectMenuDTO menu : listAllMenu) {
            for(Map<String,Object> m:ltMenuAkses){
                if(menu.getId()==Long.parseLong(m.get("id").toString())){
                    selectedMenuDTO.add(menu);
                    break;
                }
            }
        }
        Set<Long> menuSelected = selectedMenuDTO.stream().map(SelectMenuDTO::getId).collect(Collectors.toSet());
        model.addAttribute("data",mapData);
        model.addAttribute("listMenu",listAllMenu);
        model.addAttribute("menuSelected", menuSelected);
    }

    public List<SelectMenuDTO> getAllMenu(List<Map<String,Object>> ltMenu){
        List<SelectMenuDTO> selectMenuDTOS = new ArrayList<>();
        SelectMenuDTO selectMenuDTO = null;
        for(Map<String,Object> menu:ltMenu){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(Long.parseLong(menu.get("id").toString()));
            selectMenuDTO.setNama(menu.get("nama").toString());
            selectMenuDTOS.add(selectMenuDTO);
        }
        return selectMenuDTOS;
    }
}