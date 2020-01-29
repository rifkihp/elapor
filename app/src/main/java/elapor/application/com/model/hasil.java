package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class hasil implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    
    private String tipe;
    private String judul_kategori;
    private String kategori;
    
    //TIPE DOWNLOAD
    private String konten;
    private String link_download;
    
    //TIPE RANKING
    private String nama_team;   
    private String nama_atlit;          
    private String ranking;
    
    private String gambar;
    
    public hasil(int id, String tipe, String judul_kategori, String kategori, String konten, String link_download, String nama_team, String nama_atlit, String ranking, String gambar) {
        this.id = id;
        this.tipe = tipe;
        this.judul_kategori = judul_kategori;
        this.kategori = kategori;
        
        this.konten = konten;
        this.link_download = link_download;

        this.nama_team = nama_team;
        this.nama_atlit = nama_atlit;
        this.ranking = ranking;
        
        this.gambar = gambar;
    }

    public int getId() {
        return this.id;
    }

    public String getTipe() {
        return this.tipe;
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

    public String getNama_team() {
        return this.nama_team;
    }

    public String getNama_atlit() {
        return this.nama_atlit;
    }

    public String getRanking() {
        return this.ranking;
    }

    public String getGambar() {
        return this.gambar;
    }


}


