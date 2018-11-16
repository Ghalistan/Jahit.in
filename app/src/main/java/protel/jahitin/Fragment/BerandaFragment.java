package protel.jahitin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import protel.jahitin.Activity.Pembelian;
import protel.jahitin.Adapter.OverviewPagerAdapter;
import protel.jahitin.Adapter.TokoAdapter;
import protel.jahitin.Model.Toko;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

public class BerandaFragment extends Fragment
        implements TokoAdapter.BerandaClickListener {
    private RecyclerView tokoRecyclerView;
    private ViewPager viewPager;
    private TokoAdapter tokoAdapter;
    private List<Toko> listToko;
    private List<String> listKey;
    private List<Integer> listCarousel;
    private OverviewPagerAdapter pagerAdapter;
    private View view;
    private ProgressBar progressBar;
    private ProgressBarUtils pbUtils;
    public Timer timer;
    public int page = 0;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tokoDatabaseReference;
    private ValueEventListener valueEventListener;

    public static final String EXTRA_NAMA_TOKO = "nama_toko";
    public static final String EXTRA_KEY_TOKO = "key_toko";

    public BerandaFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);
        progressBar = view.findViewById(R.id.pb_fragment_beranda);
        pbUtils = new ProgressBarUtils();


        Toolbar toolbar = view.findViewById(R.id.beranda_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // Recycler view
        initRecyclerView();

        // View Pager
        initViewPager();

        firebaseDatabase = FirebaseDatabase.getInstance();

        tokoDatabaseReference = firebaseDatabase.getReference().child("toko");

        listKey = new ArrayList<>();
        attachDatabaseReadListener();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();

        if(timer != null){
            timer.cancel();
        }
    }

    @Override
    public void onTokoItemClick(int clickedItemIndex) {
        String namaToko = listToko.get(clickedItemIndex).getNama();
        String keyToko = listKey.get(clickedItemIndex);

        Intent intent = new Intent(getActivity(), Pembelian.class);
        intent.putExtra(EXTRA_NAMA_TOKO, namaToko);
        intent.putExtra(EXTRA_KEY_TOKO, keyToko);
        startActivity(intent);
    }

    public void initRecyclerView(){
        tokoRecyclerView = view.findViewById(R.id.rv_beranda_toko);
        listToko = new ArrayList<>();
        tokoAdapter = new TokoAdapter(listToko, getActivity(),  this);
        // Make smooth scroll
        ViewCompat.setNestedScrollingEnabled(tokoRecyclerView, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        tokoRecyclerView.setLayoutManager(layoutManager);
        tokoRecyclerView.setAdapter(tokoAdapter);
    }

    public void initViewPager(){
        viewPager = view.findViewById(R.id.vp_overview);
        listCarousel = new ArrayList<>();
        listCarousel.add(R.drawable.carousel_1);
        listCarousel.add(R.drawable.carousel_2);
        listCarousel.add(R.drawable.carousel_3);

        pagerAdapter = new OverviewPagerAdapter(listCarousel, getActivity());
        viewPager.setAdapter(pagerAdapter);
        pageSwitcher(5);
    }

    public void attachDatabaseReadListener(){
        if(valueEventListener == null){
            pbUtils.showLoadingIndicator(progressBar);
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Toko toko;
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        toko = data.getValue(Toko.class);
                        //Log.d("Key", data.getKey());
                        listToko.add(toko);
                        listKey.add(data.getKey());
                    }

                    tokoAdapter.notifyDataSetChanged();
                    pbUtils.hideLoadingIndicator(progressBar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pbUtils.hideLoadingIndicator(progressBar);
                    tokoAdapter.notifyDataSetChanged();
                }
            };

            tokoDatabaseReference.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void detachDatabaseReadListener(){

        if(valueEventListener != null){
            tokoDatabaseReference.removeEventListener(valueEventListener);
            valueEventListener = null;
        }
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            BerandaFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    page = (page > 2) ? 0 : page;
                    viewPager.setCurrentItem(page);
                    page++;
                }
            });

        }
    }

}
