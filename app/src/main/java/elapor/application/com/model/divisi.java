package elapor.application.com.model;

import java.io.Serializable;

public class divisi implements Serializable {

	private static final long serialVersionUID = 1L;

	String event, divisi;

	public divisi(String event, String divisi) {
		this.event= event;
		this.divisi = divisi;
	}

	public String getEvent() {
		return this.event;
	}

	public String getDivisi() {
		return this.divisi;
	}

}
