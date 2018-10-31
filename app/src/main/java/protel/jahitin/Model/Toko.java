package protel.jahitin.Model;

public class Toko {
    private String nama;
    private double rating;
    private String imageUrl;

    public Toko() {}

    public Toko(String nama, double rating, String imageUrl) {
        this.nama = nama;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

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
}
