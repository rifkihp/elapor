package elapor.application.com.model;

import java.io.Serializable;

public class banner implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id_ads;
	private String nama_ads, url_link, url_image;
	
	public banner(int id_ads, String nama_ads, String url_link, String url_image) {
		this.id_ads    = id_ads;
		this.nama_ads  = nama_ads;
		this.url_link  = url_link;
		this.url_image = url_image;
	}

	public int getId_ads() {
		return this.id_ads;
	}
	
	public String getNama_ads() {
		return this.nama_ads;
	}
	
	public String getUrl_link() {
		return this.url_link;
	}
	
	public String getUrl_image() {
		return this.url_image;
	}
}
