package protel.jahitin.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import protel.jahitin.Adapter.PembelianPagerAdapater;
import protel.jahitin.Adapter.TransaksiPagerAdapter;
import protel.jahitin.Adapter.TransaksiPembelianAdapter;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiFragment extends Fragment {
    private View view;


    public TransaksiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaksi, container, false);

        Toolbar toolbar = view.findViewById(R.id.transaksi_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ViewPager viewPager = view.findViewById(R.id.vp_transaksi);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs_transaksi);
        tabLayout.setupWithViewPager(viewPager);

        // Inflate the layout for this fragment
        return view;
    }

    public void setupViewPager(ViewPager viewPager){
        TransaksiPagerAdapter fragmentPagerAdapter = new TransaksiPagerAdapter(getChildFragmentManager(), getActivity());
        fragmentPagerAdapter.addFrag(new TransaksiPembelianFragment());
        fragmentPagerAdapter.addFrag(new TransaksiPenjualanFragment());
        viewPager.setAdapter(fragmentPagerAdapter);
    }
}
