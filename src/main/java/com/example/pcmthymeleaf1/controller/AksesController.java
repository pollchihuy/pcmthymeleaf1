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
import java.util.stream.Collectors;

@Controller
@RequestMapping("akses")
public class AksesController {

    @Autowired
    private AksesService aksesService;

    @Autowired
    private MenuService menuService;

    private Map<String,Object> filterColumn=new HashMap<>();
    public AksesController() {
        filterColumn.put("nama","Nama Akses");
    }
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
        GlobalFunction.statusOK(model,"akses",webRequest,map,filterColumn);
        return ListPage.aksesMainPage;
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
            response = aksesService.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFound(model,"akses",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.menuMainPage;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOK(model,"akses",webRequest,map,filterColumn);
        return ListPage.menuMainPage;
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
            response = aksesService.findByParam("Bearer "+jwt,sort,sortBy,page,size,column,value);
        }catch (Exception e){
            GlobalFunction.statusNotFoundDataMaster(model,"akses",webRequest,filterColumn,e.getCause().getMessage());
            return ListPage.dataTableModals;
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        GlobalFunction.statusOKDataMaster(model,"akses",webRequest,map,filterColumn);
        model.addAttribute("idComp", idComp);
        model.addAttribute("descComp",descComp);
        return ListPage.dataTableModals;
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
            response= aksesService.downloadPDF("Bearer "+jwt,column,value);
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
            response= aksesService.downloadExcel("Bearer "+jwt,column,value);
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
        Long data1[] = new Long[listAllMenuSize];//menampung data id dari all menu
        String data2[] = new String[listAllMenuSize];//menampung data nama dari all menu
        Long data3[] = new Long[listMenuAksesSize];//menampung data id dari menu akses
        String data4[] = new String[listMenuAksesSize];//menampung data nama dari menu akses

        int i=0;
        int j=0;
        for (SelectMenuDTO menu : listAllMenu) {
            data1[i]=menu.getId();
            data2[i]=menu.getNama();
            for(Map<String,Object> m:ltMenuAkses){
                if(menu.getId()==Long.parseLong(m.get("id").toString())){
                    data3[j]=menu.getId();
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
        Long data1[] = (Long[]) webRequest.getAttribute("data1",1);//menampung data id dari all menu
        String data2[] = (String[]) webRequest.getAttribute("data2",1);//menampung data nama dari all menu
        Long data3[] = (Long[]) webRequest.getAttribute("data3",1);//menampung data id dari menu akses
        String data4[] = (String[]) webRequest.getAttribute("data4",1);//menampung data nama dari menu akses
        List<SelectMenuDTO> selectedMenuDTO = new ArrayList<>();
        List<SelectMenuDTO> listAllMenu = new ArrayList<>();
        SelectMenuDTO selectMenuDTO = null;
        for(int i=0;i<data1.length;i++){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(data1[i]);
            selectMenuDTO.setNama(data2[i]);
            listAllMenu.add(selectMenuDTO);
        }

        for(int i=0;i<data3.length;i++){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(data3[i]);
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