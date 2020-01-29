package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class informasi implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String no_temuan;
    private String tanggal;
    private String judul;
    private String header;
    private String konten;
    private String link_download;
    private String gambar;
    private String status;
    private String pic;
    private String tipe;

    public informasi(int id, String no_temuan, String tanggal, String judul, String header, String konten, String link_download, String gambar, String status, String pic, String tipe) {
        this.id = id;
        this.no_temuan = no_temuan;
        this.tanggal = tanggal;
        this.judul = judul;
        this.header  = header;
        this.konten = konten;
        this.link_download = link_download;
        this.gambar = gambar;
        this.status = status;
        this.pic = pic;
        this.tipe = tipe;
    }

    public int getId() {
        return this.id;
    }

    public String getNo_temuan() {
        return this.no_temuan;
    }

    public String getTanggal() {
        return this.tanggal;
    }

    public String getHeader() {
        return this.header;
    }

    public String getJudul() {
        return this.judul;
    }

    public String getKonten() {
        return this.konten;
    }

    public String getLink_download() {
        return this.link_download;
    }

    public String getGambar() {
        return this.gambar;
    }

    public String getStatus() {
        return this.status;
    }

    public String getPic() {
        return this.pic;
    }

    public String getTipe() {
        return this.tipe;
    }

}


