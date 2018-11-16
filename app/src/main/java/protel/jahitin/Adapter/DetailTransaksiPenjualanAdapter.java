package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

public class DetailTransaksiPenjualanAdapter extends RecyclerView.Adapter<DetailTransaksiPenjualanAdapter.DetailVH>{
    private Context mContext;
    private List<Pakaian> listPakaian;
    private List<Object> listJumlah;

    public DetailTransaksiPenjualanAdapter(Context mContext, List<Pakaian> listPakaian, List<Object> listJumlah) {
        this.mContext = mContext;
        this.listPakaian = listPakaian;
        this.listJumlah = listJumlah;
    }

    @NonNull
    @Override
    public DetailTransaksiPenjualanAdapter.DetailVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_detail_transaksi_penjualan, parent, false);
        return new DetailTransaksiPenjualanAdapter.DetailVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailTransaksiPenjualanAdapter.DetailVH holder, int position) {
        Pakaian p = listPakaian.get(position);
        Long jumlah = (Long) listJumlah.get(position);

        holder.nama.setText(p.getNama());
        holder.harga.setText(String.valueOf(p.getHarga() * jumlah));
        holder.jumlah.setText(jumlah.toString());

        Glide.with(mContext).load(p.getImageUrl())
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    public class DetailVH extends RecyclerView.ViewHolder{
        private ImageView gambar;
        private TextView nama, harga, jumlah;

        public DetailVH(View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.iv_detail_penjualan);
            nama = itemView.findViewById(R.id.tv_nama_detail_penjualan);
            harga = itemView.findViewById(R.id.tv_harga_detail_penjualan);
            jumlah = itemView.findViewById(R.id.tv_jumlah_detail_penjualan);
        }
    }
}
