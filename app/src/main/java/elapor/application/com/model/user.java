package elapor.application.com.model;

import java.io.Serializable;

public class user implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String nama;
	String bagian;
	String userid;
	String pihak;
	String alamat;
	String phone;
	String comdiv;
	String photo;

	public user(int id, String nama, String bagian, String userid, String pihak, String alamat, String phone, String comdiv, String photo) {
		this.id = id;
		this.nama = nama;
		this.bagian = bagian;
		this.userid = userid;
		this.pihak = pihak;
		this.alamat = alamat;
		this.phone = phone;
		this.comdiv = comdiv;
		this.photo = photo;
	}
	
	public int getId() {
		return this.id;		
	}

	public String getNama() {
		return this.nama;
	}

	public String getBagian() {
		return this.bagian;
	}

	public String getUserid() {
		return this.userid;
	}

	public String getPihak() {
		return this.pihak;
	}

	public String getAlamat() {
		return this.alamat;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getComdiv() {
		return this.comdiv;
	}

	public String getPhoto() {
		return this.photo;
	}

}
