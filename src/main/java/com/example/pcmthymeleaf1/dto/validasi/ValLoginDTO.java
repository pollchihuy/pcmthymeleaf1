package com.example.pcmthymeleaf1.dto.validasi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ValLoginDTO {

    @NotNull(message = "Username Tidak Boleh Null")
    @NotEmpty(message = "Username Tidak Boleh Kosong")
    @NotBlank(message = "Username Tidak Boleh Blank")
    private String username;

    @NotNull(message = "Password Tidak Boleh Null")
    @NotEmpty(message = "Password Tidak Boleh Kosong")
    @NotBlank(message = "Password Tidak Boleh Blank")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
