package elapor.application.com.model;

import java.io.Serializable;

public class mendali implements Serializable {
	private static final long serialVersionUID = 1L;
	int id, emas, perak, perunggu, total;
	String kontingen, logo;
	
	public mendali(int id, String kontingen, String logo, int emas, int perak, int perunggu, int total) {

		this.id = id;
		this.kontingen = kontingen;
		this.logo = logo;
		this.emas = emas;
		this.perak = perak;
		this.perunggu = perunggu;
		this.total = total;
	}

	public int getId() {
		return this.id;
	}

	public String getKontingen() {
		return this.kontingen;
	}

	public String getLogo() {
		return this.logo;
	}
	
	public int getEmas() {
		return this.emas;
	}

	public int getPerak() {
		return this.perak;
	}

	public int getPerunggu() {
		return this.perunggu;
	}

	public int getTotal() {
		return this.total;
	}
}
