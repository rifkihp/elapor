package elapor.application.com.model;

import java.io.Serializable;

public class atlit implements Serializable {
	private static final long serialVersionUID = 1L;
	int id, total_juara;
	String first_name, last_name, sex, bod, wtf_gal, photo;

	public atlit(int id, String first_name, String last_name, String sex, String bod, String wtf_gal, int total_juara, String photo) {

		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.sex = sex;
		this.bod = bod;
		this.wtf_gal = wtf_gal;
		this.photo = photo;
		this.total_juara = total_juara;
	}

	public int getId() {
		return this.id;
	}

	public String getFirst_name() {
		return this.first_name;
	}

	public String getLast_name() {
		return this.last_name;
	}

	public String getSex() {
		return this.sex;
	}

	public String getBod() {
		return this.bod;
	}

	public String getWtf_gal() {
		return this.wtf_gal;
	}

	public String getPhoto() {
		return this.photo;
	}

	public int getTotal_juara() {
		return this.total_juara;
	}
}
