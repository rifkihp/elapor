package elapor.application.com.model;

import java.io.Serializable;

public class notifikasi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	int id;
	private String tanggal_jam, judul, konten, tipe;
	public notifikasi(int id, String tanggal_jam, String judul, String konten, String tipe) {
		this.id = id;
		this.tanggal_jam = tanggal_jam;
		this.judul = judul;
		this.konten = konten;
		this.tipe = tipe;
	}
	
	public int getId() {
		return this.id;
	}

	public String getTanggal_jam() {
		return this.tanggal_jam;
	}
	
	public String getJudul() {
		return this.judul;
	}
	
	public String getKonten() {
		return this.konten;
	}
	
	public String getTipe() {
		return this.tipe;
	}
	
}
