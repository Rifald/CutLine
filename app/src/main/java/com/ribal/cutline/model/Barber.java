package com.ribal.cutline.model;

import java.io.Serializable;

public class Barber implements Serializable {
    private String nama;
    private String desc;
    private String alamat;
    private  String url;
    private int harga;
    private String contact;

    public Barber(){
        
    }

    public Barber(String nama, String desc, String alamat, String url, int harga, String contact) {
        this.nama = nama;
        this.desc = desc;
        this.alamat = alamat;
        this.url = url;
        this.harga = harga;
        this.contact = contact;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
