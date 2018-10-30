package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder> {
    private Context mContext;
    private List<Pakaian> listPakaian;
    private KeranjangClickListener listener;

    public KeranjangAdapter(Context mContext, List<Pakaian> listPakaian, KeranjangClickListener listener) {
        this.mContext = mContext;
        this.listPakaian = listPakaian;
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
    public void onBindViewHolder(@NonNull KeranjangViewHolder holder, int position) {
        Pakaian pakaian = listPakaian.get(position);
        holder.nama.setText(pakaian.getNama());
        holder.harga.setText(String.valueOf(pakaian.getHarga()));

        Glide.with(mContext).load(pakaian.getIdGambar())
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    public class KeranjangViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
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

            btnHapus.setOnClickListener(this);
            btnPlus.setOnClickListener(this);
            btnMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface KeranjangClickListener{
        void onHapusClick(int clickedItemIndex);

        void onMinusClick(int clickedItemIndex, View view);

        void onPlusClick(int clickedItemIndex, View view);
    }
}
