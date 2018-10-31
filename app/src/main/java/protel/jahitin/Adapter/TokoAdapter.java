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

import protel.jahitin.Model.Toko;
import protel.jahitin.R;

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.TokoViewHolder> {
    private List<Toko> listToko;
    private Context mContext;
    private BerandaClickListener berandaOnClickListener;

    public TokoAdapter(List<Toko> listToko, Context mContext, BerandaClickListener mOnClickListener) {
        this.listToko = listToko;
        this.mContext = mContext;
        this.berandaOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public TokoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_toko, parent, false);
        return new TokoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokoViewHolder holder, int position) {
        Toko toko = listToko.get(position);
        holder.namaToko.setText(toko.getNama());
        holder.ratingToko.setText(String.valueOf(toko.getRating()));

        Glide.with(mContext).load(toko.getImageUrl())
                .into(holder.gambarToko);
    }

    @Override
    public int getItemCount() {
        return listToko.size();
    }

    /**
     * View Holder
     */
    public class TokoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView gambarToko;
        private TextView namaToko, ratingToko, statusToko;

        private TokoViewHolder(View itemView) {
            super(itemView);

            gambarToko = itemView.findViewById(R.id.iv_item_toko);
            namaToko = itemView.findViewById(R.id.tv_nama_toko);
            ratingToko = itemView.findViewById(R.id.tv_rating_toko);
            //statusToko = itemView.findViewById(R.id.tv_status_toko);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            berandaOnClickListener.onTokoItemClick(position);
        }
    }


    public interface BerandaClickListener {
        void onTokoItemClick(int clickedItemIndex);
    }
}
