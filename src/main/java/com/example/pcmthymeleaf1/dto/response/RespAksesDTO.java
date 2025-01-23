package com.example.pcmthymeleaf1.dto.response;

import java.util.List;

public class RespAksesDTO {

    private String nama;

    private List<RespMenuDTO> ltMenu;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public List<RespMenuDTO> getLtMenu() {
        return ltMenu;
    }

    public void setLtMenu(List<RespMenuDTO> ltMenu) {
        this.ltMenu = ltMenu;
    }
}
