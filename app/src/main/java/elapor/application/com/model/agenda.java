package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String agenda;
    private String tanggal;
    private String dari_pukul;
    private String hingga_pukul;
    private String link_download;

    public agenda(int id, String agenda, String tanggal, String dari_pukul, String hingga_pukul, String link_download) {
        this.id = id;
        this.agenda = agenda;
        this.tanggal = tanggal;
        this.dari_pukul  = dari_pukul;
        this.hingga_pukul = hingga_pukul;
        this.link_download = link_download;
    }

    public int getId() {
        return this.id;
    }

    public String getTanggal() {
        return this.tanggal;
    }

    public String getDari_pukul() {
        return this.dari_pukul;
    }

    public String getAgenda() {
        return this.agenda;
    }

    public String getHingga_pukul() {
        return this.hingga_pukul;
    }

    public String getLink_download() {
        return this.link_download;
    }

}


