package elapor.application.com.model;

import java.io.Serializable;

public class peserta implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String nama, email, alamat, phone, lokasi_kerja, jenis_kelamin, photo;

	public peserta(int id, String nama, String email, String alamat, String phone, String lokasi_kerja, String jenis_kelamin, String photo) {
		this.id = id;
		this.nama = nama;
		this.email = email;
		this.alamat = alamat;
		this.phone = phone;
		this.lokasi_kerja = lokasi_kerja;
		this.jenis_kelamin = jenis_kelamin;
		this.photo = photo;
	}
	
	public int getId() {
		return this.id;		
	}

	public String getNama() {
		return this.nama;
	}

	public String getEmail() {
		return this.email;
	}

	public String getAlamat() {
		return this.alamat;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getLokasi_kerja() {
		return this.lokasi_kerja;
	}

	public String getJenis_kelamin() {
		return this.jenis_kelamin;
	}

	public String getPhoto() {
		return this.photo;
	}
}
