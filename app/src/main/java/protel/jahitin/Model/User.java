package protel.jahitin.Model;

public class User {
    private String alamat;
    private int jumlahTransaksi, isiKeranjang, jumlahWishlist;

    public User() {
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(int jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public int getIsiKeranjang() {
        return isiKeranjang;
    }

    public void setIsiKeranjang(int isiKeranjang) {
        this.isiKeranjang = isiKeranjang;
    }

    public int getJumlahWishlist() {
        return jumlahWishlist;
    }

    public void setJumlahWishlist(int jumlahWishlist) {
        this.jumlahWishlist = jumlahWishlist;
    }
}
