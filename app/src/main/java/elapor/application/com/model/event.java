package elapor.application.com.model;

import java.io.Serializable;

public class event implements Serializable {
	private static final long serialVersionUID = 1L;
	int id, total_peserta;
	String judul, tanggal, lokasi, selesai, banner1, banner2, banner3, logo;
	boolean is_open;

	public event(int id, String judul, String tanggal, String lokasi, String selesai, int total_peserta, boolean is_open, String banner1, String banner2, String banner3, String logo) {
		this.id = id;
		this.judul = judul;
		this.tanggal = tanggal;
		this.lokasi = lokasi;
		this.selesai = selesai;
		this.total_peserta = total_peserta;
		this.is_open = is_open;
		this.banner1 = banner1;
		this.banner2 = banner2;
		this.banner3 = banner3;
		this.logo = logo;
	}

	public int getId() {
		return this.id;
	}

	public String getJudul() {
		return this.judul;
	}

	public String getTanggal() {
		return this.tanggal;
	}
	
	public String getLokasi() {
		return this.lokasi;
	}

	public String getSelesai() {
		return this.selesai;
	}

	public int getTotal_peserta() {
		return this.total_peserta;
	}

	public boolean getIs_open() {
		return this.is_open;
	}

	public String getBanner1() {
		return this.banner1;
	}

	public String getBanner2() {
		return this.banner2;
	}

	public String getBanner3() {
		return this.banner3;
	}

	public String getLogo() {
		return this.logo;
	}
}
