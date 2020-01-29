package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class gallery implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String keterangan;
    private String gambar;

    public gallery(int id, String keterangan, String gambar) {
        this.id = id;
        this.keterangan = keterangan;
        this.gambar = gambar;
    }

    public int getId() {
        return this.id;
    }

    public String getKeterangan() {
        return this.keterangan;
    }

    public String getGambar() {
        return this.gambar;
    }

}


