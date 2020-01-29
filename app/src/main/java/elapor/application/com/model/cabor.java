package elapor.application.com.model;

import java.io.Serializable;

public class cabor implements Serializable {

	private static final long serialVersionUID = 1L;

	int id;
	String nama;

	public cabor(int id, String nama) {
		this.id = id;
		this.nama = nama;
	}

	public String getNama() {
		return this.nama;
	}

	public int getId() {
		return this.id;
	}

}
