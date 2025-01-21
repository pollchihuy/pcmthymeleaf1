package com.example.pcmthymeleaf1.model;


import java.util.Date;
import java.util.List;
public class Menu {

    private Long id;

    private String nama;

    private String path;

    private List<Akses> ltAkses;

    private GroupMenu groupMenu;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Akses> getLtAkses() {
        return ltAkses;
    }

    public void setLtAkses(List<Akses> ltAkses) {
        this.ltAkses = ltAkses;
    }

    public GroupMenu getGroupMenu() {
        return groupMenu;
    }

    public void setGroupMenu(GroupMenu groupMenu) {
        this.groupMenu = groupMenu;
    }
}
