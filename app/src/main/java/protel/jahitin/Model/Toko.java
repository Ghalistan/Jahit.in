package protel.jahitin.Model;

public class Toko {
    private String nama;
    private double rating;
    private String status;
    private int imageId;

    public Toko(String nama, double rating, String status, int imageId) {
        this.nama = nama;
        this.rating = rating;
        this.status = status;
        this.imageId = imageId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
