package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import protel.jahitin.R;

public class OverviewPagerAdapter extends PagerAdapter {
    private List<Integer> images;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public OverviewPagerAdapter(List<Integer> images, Context context) {
        this.images = images;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_carousel, container, false);

        ImageView imageView = itemView.findViewById(R.id.iv_overview);
        Glide.with(mContext).load(images.get(position))
                .into(imageView);
        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
