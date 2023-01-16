package com.example.deneme;

public class MesajModel {
    private String mesajAdi, mesaj, uid;

    public MesajModel(String mesajAdi, String mesaj, String uid) {
        this.mesajAdi = mesajAdi;
        this.mesaj = mesaj;
        this.uid = uid;
    }

    public String getMesajAdi() {
        return mesajAdi;
    }

    public String getMesaj() {
        return mesaj;
    }

    public String getUid() {
        return uid;
    }

    public void setMesajAdi(String mesajAdi) {
        this.mesajAdi = mesajAdi;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
