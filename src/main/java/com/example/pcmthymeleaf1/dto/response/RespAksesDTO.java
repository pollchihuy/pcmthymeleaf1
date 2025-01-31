package com.example.pcmthymeleaf1.dto.response;

import java.util.List;

public class RespAksesDTO {

    private Long id;
    private String nama;

    private List<RespMenuDTO> ltMenu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
