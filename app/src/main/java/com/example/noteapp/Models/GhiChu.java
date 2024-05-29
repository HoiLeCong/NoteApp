package com.example.noteapp.Models;

import java.io.Serializable;
import java.util.Arrays;

public class GhiChu implements Serializable {
    int ID;
    String title, noiDung;
    String createDate, dueDate;
    byte []hinhAnh;
    int complete;
    int listDelete;
    int IDLoaiGhiChu;


    public GhiChu(int ID, String title, String noiDung, String createDate, String dueDate, byte []hinhAnh,int complete, int listDelete, int IDLoaiGhiChu) {
        this.ID = ID;
        this.title = title;
        this.noiDung = noiDung;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.hinhAnh = hinhAnh;
        this.complete = complete;
        this.listDelete = listDelete;
        this.IDLoaiGhiChu = IDLoaiGhiChu;
    }

    public GhiChu() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getIDLoaiGhiChu() {
        return IDLoaiGhiChu;
    }

    public void setIDLoaiGhiChu(int IDLoaiGhiChu) {
        this.IDLoaiGhiChu = IDLoaiGhiChu;
    }


    public int getComplete() {return complete;}

    public int getListDelete() { return listDelete;}

    public void setListDelete(int listDelete) {this.listDelete = listDelete;}

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "GhiChu{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", noiDung='" + noiDung + '\'' +
                ", createDate='" + createDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", hinhAnh=" + Arrays.toString(hinhAnh) +
                ", complete=" + complete +
                ", IDLoaiGhiChu=" + IDLoaiGhiChu +
                '}';
    }
}
