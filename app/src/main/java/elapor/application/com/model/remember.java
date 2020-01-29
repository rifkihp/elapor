package elapor.application.com.model;

import java.io.Serializable;

public class remember implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String email, password, checklist;

	public remember(String email, String password, String checklist) {
		this.email = email;
		this.password = password;
		this.checklist = checklist;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}

	public String getChecklist() {
		return this.checklist;
	}
}
