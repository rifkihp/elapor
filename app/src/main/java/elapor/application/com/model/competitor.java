package elapor.application.com.model;

import java.io.Serializable;

public class competitor implements Serializable {
	private static final long serialVersionUID = 1L;
	int id, grup, rank;
	String kategori, nama, team, divisi, jenis_kelamin, kelas, photo;

	public competitor(int id, String kategori, String nama, String team, int grup, String divisi, String jenis_kelamin, String kelas, int rank, String photo) {

		this.id = id;
		this.kategori = kategori;
		this.nama = nama;
		this.team = team;
		this.grup = grup;
		this.divisi = divisi;
		this.jenis_kelamin = jenis_kelamin;
		this.kelas = kelas;
		this.rank = rank;
		this.photo = photo;
	}

	public int getId() {
		return this.id;
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

	public int getGrup() {
		return this.grup;
	}

	public String getDivisi() {
		return this.divisi;
	}

	public String getJenis_kelamin() {
		return this.jenis_kelamin;
	}

	public String getKelas() {
		return this.kelas;
	}

	public int getRank() {
		return this.rank;
	}

	public String getPhoto() {
		return this.photo;
	}
}
