package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class teaser implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String thumbnail;
    private String filename;
    private double ukuran;

    public teaser(int id, String thumbnail, String filename, double ukuran) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.filename = filename;
        this.ukuran = ukuran;
    }

    public int getId() {
        return this.id;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getFilename() {
        return this.filename;
    }

    public double getUkuran() {
        return this.ukuran;
    }

}


