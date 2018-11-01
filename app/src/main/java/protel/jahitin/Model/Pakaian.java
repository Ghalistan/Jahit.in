package protel.jahitin.Model;

import java.util.HashMap;

public class Pakaian {
    private String nama, bahan, jenis, imageUrl;
    private int harga, gender;
    private HashMap<String, String> ukuranTersedia, warnaTersedia;

    public static final int GENDER_PRIA = 0;
    public static final int GENDER_WANITA = 1;

    public Pakaian() {
    }

    public Pakaian(String nama, String bahan, String jenis, String imageUrl, int harga, int gender,
                   HashMap<String, String> ukuranTersedia, HashMap<String, String> warnaTersedia)
    {
        this.nama = nama;
        this.bahan = bahan;
        this.jenis = jenis;
        this.imageUrl = imageUrl;
        this.harga = harga;
        this.gender = gender;
        this.ukuranTersedia = ukuranTersedia;
        this.warnaTersedia = warnaTersedia;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public HashMap<String, String> getUkuranTersedia() {
        return ukuranTersedia;
    }

    public void setUkuranTersedia(HashMap<String, String> ukuranTersedia) {
        this.ukuranTersedia = ukuranTersedia;
    }

    public HashMap<String, String> getWarnaTersedia() {
        return warnaTersedia;
    }

    public void setWarnaTersedia(HashMap<String, String> warnaTersedia) {
        this.warnaTersedia = warnaTersedia;
    }
}
