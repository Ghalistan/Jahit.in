package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import protel.jahitin.R;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private Context mContext;
    private CarouselClickListener mClickListener;
    private List<Integer> images;



    public CarouselAdapter(Context mContext, List<Integer> images, CarouselClickListener listener) {
        Log.d("CarouselAdapter", "created");
        this.mContext = mContext;
        this.mClickListener = listener;
        this.images = images;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForCarousel = R.layout.item_carousel;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForCarousel, parent, false);

        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
         Integer imageId = images.get(position);
         Glide.with(mContext).load(imageId)
                 .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private ImageView imageView;

        private CarouselViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_overview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickListener.onCarouselItemClick(position);
        }
    }

    public interface CarouselClickListener{
        void onCarouselItemClick(int clickedItemIndex);
    }
}
