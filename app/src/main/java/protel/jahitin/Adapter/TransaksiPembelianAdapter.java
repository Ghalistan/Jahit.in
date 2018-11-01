package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;

public class TransaksiPembelianAdapter extends RecyclerView.Adapter<TransaksiPembelianAdapter.TransaksiPembelianVH> {
    private Context mContext;
    private List<Transaksi> listTransaksi;
    private TransaksiPembelianClickListener listener;
    private Transaksi transaksi;

    public TransaksiPembelianAdapter(Context mContext, List<Transaksi> listTransaksi, TransaksiPembelianClickListener listener) {
        this.mContext = mContext;
        this.listTransaksi = listTransaksi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransaksiPembelianVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_transaksi, parent, false);
        return new TransaksiPembelianVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiPembelianVH holder, int position) {
        transaksi = listTransaksi.get(position);

        holder.noRekTV.setText(transaksi.getRekTujuan());
        String stringJumlahBayar = "Rp " + String.valueOf(transaksi.getTotalHarga());
        holder.jumlahBayarTV.setText(stringJumlahBayar);

        setIconBank(holder);
        setStatusPembayaran(holder);
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class TransaksiPembelianVH extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private TextView noRekTV, jumlahBayarTV, statusPembayaranTV;
        private ImageView iconBank;
        private CardView container;

        public TransaksiPembelianVH(View itemView) {
            super(itemView);
            noRekTV = itemView.findViewById(R.id.tv_norek_transaksi_pembelian);
            jumlahBayarTV = itemView.findViewById(R.id.tv_jumlah_bayar_transaksi);
            statusPembayaranTV = itemView.findViewById(R.id.tv_status_transaksi);
            iconBank = itemView.findViewById(R.id.iv_icon_bank_transaksi);
            container = itemView.findViewById(R.id.cv_transaksi_pembelian);

            container.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public interface TransaksiPembelianClickListener{
        void onItemClick(int clickedItemIndex);
    }

    public void setIconBank(TransaksiPembelianVH holder){
        int idIcon;
        switch (transaksi.getCaraBayar()){
            case "Mandiri":
                idIcon = R.drawable.mandiri;
                break;
            default:
                idIcon = -1;
                break;
        }

        if(idIcon != -1){
            Glide.with(mContext).load("")
                    .placeholder(idIcon)
                    .into(holder.iconBank);
        }
    }

    public void setStatusPembayaran(TransaksiPembelianVH holder){
        if(transaksi.getStatus().equals("Menunggu Pembayaran")){
            holder.statusPembayaranTV.setTextColor(ContextCompat.getColor(mContext, R.color.menunggu_pembayaran));
        }else if(transaksi.getStatus().equals("Barang Dikirim")){
            holder.statusPembayaranTV.setTextColor(ContextCompat.getColor(mContext, R.color.barang_dikirim));
        }else if(transaksi.getStatus().equals("Barang Diterima")){
            holder.statusPembayaranTV.setTextColor(ContextCompat.getColor(mContext, R.color.barang_diterima));
        }

        holder.statusPembayaranTV.setText(transaksi.getStatus());
    }
}
