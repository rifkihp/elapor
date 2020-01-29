package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class bagan implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String judul_kategori;
    private String kategori;
    private String konten;
    private String link_download;

    public bagan(int id, String judul_kategori, String kategori, String konten, String link_download) {
        this.id = id;
        this.judul_kategori = judul_kategori;
        this.kategori = kategori;
        this.konten = konten;
        this.link_download = link_download;
    }

    public int getId() {
        return this.id;
    }

    public String getJudul_kategori() {
        return this.judul_kategori;
    }

    public String getKategori() {
        return this.kategori;
    }

    public String getKonten() {
        return this.konten;
    }

    public String getLink_download() {
        return this.link_download;
    }

}


