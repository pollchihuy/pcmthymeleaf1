package com.example.pcmthymeleaf1.dto.validasi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author pollc a.k.a. Paul Christian
Java Developer
Created on Wed 21:14
@Last Modified Wed 21:14
Version 1.0
*/
public class ValAksesDTO {

    @NotNull(message = "Field Nama Tidak Boleh NULL")
    @NotEmpty(message = "Field Nama Tidak Boleh Kosong")
    @NotBlank(message = "Field Nama Tidak Boleh Blank")
    @Pattern(regexp = "^[\\w\\s]{6,40}$",message = "AflaNumerik Dengan Spasi Min 6 Max 40")
    private String nama;

    @NotNull(message = "Menu Wajib DIISI")
    private List<ValMenuDTO> ltMenu;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public @NotNull(message = "Menu Wajib DIISI") List<ValMenuDTO> getLtMenu() {
        return ltMenu;
    }

    public void setLtMenu(@NotNull(message = "Menu Wajib DIISI") List<ValMenuDTO> ltMenu) {
        this.ltMenu = ltMenu;
    }
}
