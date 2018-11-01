package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import protel.jahitin.Model.Keranjang;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder> {
    private Context mContext;
    private List<Pakaian> listPakaian;
    private List<Keranjang> listKeranjang;
    private KeranjangClickListener listener;

    public KeranjangAdapter(Context mContext, List<Pakaian> listPakaian, List<Keranjang> listKeranjang, KeranjangClickListener listener) {
        this.mContext = mContext;
        this.listPakaian = listPakaian;
        this.listKeranjang = listKeranjang;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KeranjangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_keranjang, parent, false);
        return new KeranjangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KeranjangViewHolder holder, final int position) {
        Pakaian pakaian = listPakaian.get(position);
        holder.nama.setText(pakaian.getNama());

        Integer jumlah = listKeranjang.get(position).getJumlah();
        holder.kuantitas.setText(jumlah.toString());

        jumlah *= pakaian.getHarga();
        holder.harga.setText(jumlah.toString());

        Glide.with(mContext).load(pakaian.getImageUrl())
                .into(holder.gambar);

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = listKeranjang.get(position).getJumlah() + 1;
                holder.kuantitas.setText(String.valueOf(jumlah));
                Keranjang keranjangBaru = listKeranjang.get(position);
                keranjangBaru.setJumlah(jumlah);
                notifyDataSetChanged();

                listener.onPlusClick(position, jumlah);
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = listKeranjang.get(position).getJumlah();
                if(jumlah > 1){
                    jumlah -= 1;
                    holder.kuantitas.setText(String.valueOf(jumlah));
                    Keranjang keranjangBaru = listKeranjang.get(position);
                    keranjangBaru.setJumlah(jumlah);
                    notifyDataSetChanged();

                    listener.onMinusClick(position, jumlah);
                }
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onHapusClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    public class KeranjangViewHolder extends RecyclerView.ViewHolder{
        private ImageView gambar;
        private TextView nama, harga, kuantitas;
        private Button btnPlus, btnMinus, btnHapus;

        private KeranjangViewHolder(View itemView) {
            super(itemView);

            gambar = itemView.findViewById(R.id.iv_keranjang);
            nama = itemView.findViewById(R.id.tv_nama_keranjang);
            harga = itemView.findViewById(R.id.tv_harga_keranjang);
            kuantitas = itemView.findViewById(R.id.tv_jumlah_keranjang);

            btnHapus = itemView.findViewById(R.id.btn_hapus_keranjang);
            btnMinus = itemView.findViewById(R.id.btn_minus_keranjang);
            btnPlus = itemView.findViewById(R.id.btn_plus_keranjang);
        }
    }



    public interface KeranjangClickListener{
        void onHapusClick(int clickedItemIndex);

        void onMinusClick(int clickedItemIndex, int jumlahBaru);

        void onPlusClick(int clickedItemIndex, int jumlahBaru);
    }
}
