package protel.jahitin.Model;

import java.util.Map;

public class Toko {
    private String nama;
    private double rating;
    private String imageUrl, noTelp, alamat;
    private Map<String, Boolean> pakaian;

    public Toko() {}

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Boolean> getPakaian() {
        return pakaian;
    }

    public void setPakaian(Map<String, Boolean> pakaian) {
        this.pakaian = pakaian;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
