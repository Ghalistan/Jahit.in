package protel.jahitin.Model;

public class Pakaian {
    private String nama, bahan, ukuran, jenis, warna;
    private int harga, idGambar, gender;

    public static final int GENDER_PRIA = 1;
    public static final int GENDER_WANITA = 2;

    public Pakaian(String nama, String bahan, String ukuran, String jenis,
                   String warna, int harga, int idGambar, int gender) {
        this.nama = nama;
        this.bahan = bahan;
        this.ukuran = ukuran;
        this.jenis = jenis;
        this.warna = warna;
        this.harga = harga;
        this.idGambar = idGambar;
        this.gender = gender;
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

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getIdGambar() {
        return idGambar;
    }

    public void setIdGambar(int idGambar) {
        this.idGambar = idGambar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
