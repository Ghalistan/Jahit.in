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

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

public class  PakaianJadiAdapter extends RecyclerView.Adapter<PakaianJadiAdapter.PakaianJadiViewHolder> {
    private List<Pakaian> listPakaian;
    private Context mContext;
    private PakaianJadiClickListener listener;

    public PakaianJadiAdapter(Context mContext, List<Pakaian> listPakaian, PakaianJadiClickListener listener) {
        this.mContext = mContext;
        this.listPakaian = listPakaian;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PakaianJadiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_pakaian_jadi, parent, false);
        return new PakaianJadiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PakaianJadiViewHolder holder, int position) {
        Pakaian pakaian = listPakaian.get(position);
        holder.namaItem.setText(pakaian.getNama());
        holder.bahanItem.setText(pakaian.getBahan());

        List<Object> listUkuran = new ArrayList<>(pakaian.getUkuranTersedia());
        holder.ukuranItem.setText(String.valueOf(listUkuran.get(0)));

        String harga = "Rp " + String.valueOf(pakaian.getHarga());
        holder.hargaItem.setText(harga);

        Glide.with(mContext).load(pakaian.getImageUrl())
                .into(holder.gambarItem);
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    public class PakaianJadiViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private ImageView gambarItem;
        private TextView namaItem, bahanItem, ukuranItem, hargaItem;
        private Button btnLike, btnTambah;

        private PakaianJadiViewHolder(View itemView) {
            super(itemView);

            gambarItem = itemView.findViewById(R.id.iv_pakaian_jadi);
            namaItem = itemView.findViewById(R.id.tv_nama_pakaian_jadi);
            bahanItem = itemView.findViewById(R.id.tv_bahan_pakaian_jadi);
            ukuranItem = itemView.findViewById(R.id.tv_ukuran_pakaian_jadi);
            hargaItem = itemView.findViewById(R.id.tv_harga_pakaian_jadi);
            //btnLike = itemView.findViewById(R.id.btn_like_pakaian_jadi);
            btnTambah = itemView.findViewById(R.id.btn_tambah_pakaian_jadi);

            //btnLike.setOnClickListener(this);
            btnTambah.setOnClickListener(this);
            namaItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()){
//                case R.id.btn_like_pakaian_jadi:
//                    listener.onLikeItemClick(position, view);
//                    break;
                case R.id.btn_tambah_pakaian_jadi:
                    listener.onTambahItemClick(position, view);
                    break;
                case R.id.tv_nama_pakaian_jadi:
                    listener.onBarangItemClick(position);
                    break;
            }
        }
    }

    public interface PakaianJadiClickListener{
        void onTambahItemClick(int clickedItemIndex, View view);

        void onBarangItemClick(int clickedItemIndex);

        //void onLikeItemClick(int clickedItemIndex, View view);
    }
}
