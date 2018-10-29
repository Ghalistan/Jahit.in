package protel.jahitin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.Activity.Pembelian;
import protel.jahitin.Adapter.OverviewPagerAdapter;
import protel.jahitin.Adapter.TokoAdapter;
import protel.jahitin.Model.Toko;
import protel.jahitin.R;

public class BerandaFragment extends Fragment
        implements TokoAdapter.BerandaClickListener {
    private RecyclerView tokoRecyclerView;
    private TokoAdapter tokoAdapter;
    private List<Toko> listToko;
    private List<Integer> listCarousel;
    private OverviewPagerAdapter pagerAdapter;
    private View view;

    public static final String EXTRA_NAMA_TOKO = "nama_toko";

    public BerandaFragment() {}


    public static BerandaFragment newInstance(String param1, String param2) {
        BerandaFragment fragment = new BerandaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);

        // Recycler view
        tokoRecyclerView = view.findViewById(R.id.rv_beranda_toko);
        listToko = new ArrayList<>();
        tokoAdapter = new TokoAdapter(listToko, getActivity(),  this);
        // Make smooth scroll
        ViewCompat.setNestedScrollingEnabled(tokoRecyclerView, false);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        tokoRecyclerView.setLayoutManager(layoutManager);
        tokoRecyclerView.setAdapter(tokoAdapter);

        // View Pager
        // For View Pager
        int[] ovImages = new int[]{
                R.drawable.carousel_1,
                R.drawable.carousel_1,
                R.drawable.carousel_1,
                R.drawable.carousel_1
        };

        listCarousel = new ArrayList<>();
        for (int i = 0; i < ovImages.length; i++){
            listCarousel.add(ovImages[i]);
        }

        ViewPager viewPager = view.findViewById(R.id.vp_overview);

        pagerAdapter = new OverviewPagerAdapter(listCarousel, getActivity());
        viewPager.setAdapter(pagerAdapter);

        generateDummyList();

        // Inflate the layout for this fragment
        return view;
    }

    private void generateDummyList(){
        // For Recycler View
        int[] images = new int[]{
                R.drawable.toko_1,
                R.drawable.toko_1,
                R.drawable.toko_1,
                R.drawable.toko_1
        };

        Toko t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
        listToko.add(t);
        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
        listToko.add(t);
        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
        listToko.add(t);
        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
        listToko.add(t);
        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
        listToko.add(t);
        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
        listToko.add(t);

        tokoAdapter.notifyDataSetChanged();

        int[] ovImages = new int[]{
                R.drawable.carousel_1,
                R.drawable.carousel_1,
                R.drawable.carousel_1,
                R.drawable.carousel_1
        };

        listCarousel = new ArrayList<>();
        for (int i = 0; i < ovImages.length; i++){
            listCarousel.add(ovImages[i]);
        }

        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTokoItemClick(int clickedItemIndex) {
        String namaToko = listToko.get(clickedItemIndex).getNama();

        Intent intent = new Intent(getActivity(), Pembelian.class);
        intent.putExtra(EXTRA_NAMA_TOKO, namaToko);
        startActivity(intent);
    }
}
