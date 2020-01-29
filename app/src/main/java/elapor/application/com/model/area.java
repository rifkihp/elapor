package elapor.application.com.model;

import java.io.Serializable;

public class area implements Serializable {

	private static final long serialVersionUID = 1L;

	int id;
	String area;

	public area(int id, String area) {
		this.id = id;
		this.area = area;
	}

	public int getId() {
		return this.id;
	}

	public String getArea() {
		return this.area;
	}

}
