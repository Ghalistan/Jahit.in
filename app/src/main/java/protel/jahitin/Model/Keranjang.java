package protel.jahitin.Model;

public class Keranjang {
    private String idBarang;
    private String idToko;
    private Integer jumlah;

    public Keranjang(String idBarang, String idToko, Integer jumlah) {
        this.idBarang = idBarang;
        this.idToko = idToko;
        this.jumlah = jumlah;
    }

    public String getIdToko() {
        return idToko;
    }

    public void setIdToko(String idToko) {
        this.idToko = idToko;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Keranjang() {
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }


}
