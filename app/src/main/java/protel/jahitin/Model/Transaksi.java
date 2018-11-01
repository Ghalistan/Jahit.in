package protel.jahitin.Model;

import java.util.List;

public class Transaksi {
    private String userId, alamat, caraBayar, kurir, status, rekTujuan;
    private int hargaBarang, hargaKurir, totalHarga, buktiPembayaranUrl;
    private long waktuTransaksi;
    private List<Object> barang;

    public Transaksi() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCaraBayar() {
        return caraBayar;
    }

    public void setCaraBayar(String caraBayar) {
        this.caraBayar = caraBayar;
    }

    public String getKurir() {
        return kurir;
    }

    public void setKurir(String kurir) {
        this.kurir = kurir;
    }

    public int getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(int hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public int getHargaKurir() {
        return hargaKurir;
    }

    public void setHargaKurir(int hargaKurir) {
        this.hargaKurir = hargaKurir;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getBarang() {
        return barang;
    }

    public void setBarang(List<Object> barang) {
        this.barang = barang;
    }

    public long getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(long waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }


    public String getRekTujuan() {
        return rekTujuan;
    }

    public void setRekTujuan(String rekTujuan) {
        this.rekTujuan = rekTujuan;
    }

    public int getBuktiPembayaranUrl() {
        return buktiPembayaranUrl;
    }

    public void setBuktiPembayaranUrl(int buktiPembayaranUrl) {
        this.buktiPembayaranUrl = buktiPembayaranUrl;
    }
}