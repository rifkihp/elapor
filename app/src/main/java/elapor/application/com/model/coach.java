package elapor.application.com.model;

import java.io.Serializable;

public class coach implements Serializable {

	private static final long serialVersionUID = 1L;

	int id;
	String nama;
	String photo;

	public coach(int id, String nama, String photo) {
		this.id = id;
		this.nama = nama;
		this.photo = photo;
	}

	public int getId() {
		return this.id;
	}

	public String getNama() {
		return this.nama;
	}

	public String getPhoto() {
		return this.photo;
	}

}
