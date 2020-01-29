package elapor.application.com.model;

import java.io.Serializable;

public class bank implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String no_rekening, nama_pemilik_rekening, nama_bank, cabang, gambar;
    private boolean is_selected;

    public bank(int id, String no_rekening, String nama_pemilik_rekening, String nama_bank, String cabang, String gambar) {
        this.id = id;
        this.no_rekening = no_rekening;
        this.nama_pemilik_rekening = nama_pemilik_rekening;
        this.nama_bank = nama_bank;
        this.cabang = cabang;
        this.gambar = gambar;
        this.is_selected = false;
    }

    public int getId() {
        return this.id;
    }

    public String getNo_rekening() {
        return this.no_rekening;
    }

    public String getNama_pemilik_rekening() {
        return this.nama_pemilik_rekening;
    }

    public String getNama_bank() {
        return this.nama_bank;
    }

    public String getCabang() {
        return this.cabang;
    }

    public String getGambar() {
        return this.gambar;
    }

    public boolean getIs_selected() {
        return this.is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected=is_selected;
    }

}
