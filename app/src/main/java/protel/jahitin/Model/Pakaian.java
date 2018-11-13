package protel.jahitin.Model;

import java.util.HashMap;
import java.util.List;

public class Pakaian {
    private String nama, bahan, jenis, imageUrl, toko,
            gender, desainCustomUrl, keterangan, catatan;
    private int harga;
    private List<Object> ukuranTersedia, warnaTersedia;

    public static final int GENDER_PRIA = 0;
    public static final int GENDER_WANITA = 1;

    public Pakaian() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Object> getUkuranTersedia() {
        return ukuranTersedia;
    }

    public void setUkuranTersedia(List<Object> ukuranTersedia) {
        this.ukuranTersedia = ukuranTersedia;
    }

    public List<Object> getWarnaTersedia() {
        return warnaTersedia;
    }

    public void setWarnaTersedia(List<Object> warnaTersedia) {
        this.warnaTersedia = warnaTersedia;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getDesainCustomUrl() {
        return desainCustomUrl;
    }

    public void setDesainCustomUrl(String desainCustomUrl) {
        this.desainCustomUrl = desainCustomUrl;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
