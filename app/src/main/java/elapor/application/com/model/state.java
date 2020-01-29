package elapor.application.com.model;

import java.io.Serializable;

public class state implements Serializable {

	private static final long serialVersionUID = 1L;

	int id;
	String state;

	public state(int id, String state) {
		this.id = id;
		this.state = state;
	}

	public int getId() {
		return this.id;
	}

	public String getState() {
		return this.state;
	}

}
