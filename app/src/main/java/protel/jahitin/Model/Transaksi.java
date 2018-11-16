package protel.jahitin.Model;

import java.util.List;

public class Transaksi {
    private String userId, alamat, caraBayar, kurir,
            status, rekTujuan, buktiPembayaranUrl, noResi;
    private int hargaBarang, hargaKurir, totalHarga;
    private long waktuTransaksi;
    private List<Object> barang, jumlah;

    public Transaksi() {
    }

    public List<Object> getJumlah() {
        return jumlah;
    }

    public void setJumlah(List<Object> jumlah) {
        this.jumlah = jumlah;
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

    public String getBuktiPembayaranUrl() {
        return buktiPembayaranUrl;
    }

    public void setBuktiPembayaranUrl(String buktiPembayaranUrl) {
        this.buktiPembayaranUrl = buktiPembayaranUrl;
    }

    public String getNoResi() {
        return noResi;
    }

    public void setNoResi(String noResi) {
        this.noResi = noResi;
    }
}
