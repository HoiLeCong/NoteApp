package com.example.noteapp.Models;

import java.io.Serializable;

public class LoaiGhiChu implements Serializable {
    int ID = 0;
    String tenLoaiGhiChu;


    public LoaiGhiChu() {
    }

    public LoaiGhiChu(int ID, String tenLoaiGhiChu) {
        this.ID = ID;
        this.tenLoaiGhiChu = tenLoaiGhiChu;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenLoaiGhiChu() {
        return tenLoaiGhiChu;
    }

    public void setTenLoaiGhiChu(String tenLoaiGhiChu) {
        this.tenLoaiGhiChu = tenLoaiGhiChu;
    }

    @Override
    public String toString() {
        return "LoaiGhiChu{" +
                "ID=" + ID +
                ", tenLoaiGhiChu='" + tenLoaiGhiChu + '\'' +
                '}';
    }
}
