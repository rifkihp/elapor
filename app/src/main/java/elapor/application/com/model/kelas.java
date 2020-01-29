package elapor.application.com.model;

import java.io.Serializable;

public class kelas implements Serializable {

	private static final long serialVersionUID = 1L;
	int category_id;
	String sex, nama;

	public kelas(int category_id, String sex, String nama) {
		this.category_id = category_id;
		this.sex = sex;
		this.nama  = nama;
	}

	public int getCategory_id() {
		return this.category_id;
	}

	public String getSex() {
		return this.sex;
	}

	public String getNama() {
		return this.nama;
	}

}
