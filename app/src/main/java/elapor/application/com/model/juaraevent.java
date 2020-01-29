package elapor.application.com.model;

import java.io.Serializable;

public class juaraevent implements Serializable {
	private static final long serialVersionUID = 1L;

	int id;
	String judul, kategori, nama, team, logo;
	int rank;


	public juaraevent(int id, String judul, String kategori, String nama, String team, int rank, String logo) {

		this.id = id;
		this.judul = judul;
		this.kategori = kategori;
		this.nama = nama;
		this.team = team;
		this.rank = rank;
		this.logo = logo;
	}

	public int getId() {
		return this.id;
	}

	public String getJudul() {
		return this.judul;
	}

	public String getKategori() {
		return this.kategori;
	}

	public String getNama() {
		return this.nama;
	}

	public String getTeam() {
		return this.team;
	}

	public int getRank() {
		return this.rank;
	}

	public String getLogo() {
		return this.logo;
	}
}
