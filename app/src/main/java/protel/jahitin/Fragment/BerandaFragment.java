package protel.jahitin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private ViewPager viewPager;
    private TokoAdapter tokoAdapter;
    private List<Toko> listToko;
    private List<String> listKey;
    private List<Integer> listCarousel;
    private OverviewPagerAdapter pagerAdapter;
    private View view;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tokoDatabaseReference;
    private ChildEventListener childEventListener;

    public static final String EXTRA_NAMA_TOKO = "nama_toko";
    public static final String EXTRA_KEY_TOKO = "key_toko";

    public BerandaFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);

        Toolbar toolbar = view.findViewById(R.id.beranda_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // Recycler view
        initRecyclerView();

        // View Pager
        initViewPager();

        generateDummyList();

        firebaseDatabase = FirebaseDatabase.getInstance();

        tokoDatabaseReference = firebaseDatabase.getReference().child("toko");

        listKey = new ArrayList<>();
        attachDatabaseReadListener();


        // Inflate the layout for this fragment
        return view;
    }

    private void generateDummyList(){
//        // For Recycler View
//        int[] images = new int[]{
//                R.drawable.toko_1,
//                R.drawable.toko_1,
//                R.drawable.toko_1,
//                R.drawable.toko_1
//        };
//
//        Toko t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
//        listToko.add(t);
//        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
//        listToko.add(t);
//        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
//        listToko.add(t);
//        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
//        listToko.add(t);
//        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
//        listToko.add(t);
//        t = new Toko("Victoria Galery", 4.5, "Open", images[0]);
//        listToko.add(t);
//
//        tokoAdapter.notifyDataSetChanged();
//
        // For View Pager
        int[] ovImages = new int[]{
                R.drawable.carousel_1,
                R.drawable.carousel_1,
                R.drawable.carousel_1,
                R.drawable.carousel_1
        };

        for (int i = 0; i < ovImages.length; i++){
            listCarousel.add(ovImages[i]);
        }

        pagerAdapter.notifyDataSetChanged();
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
        pagerAdapter = new OverviewPagerAdapter(listCarousel, getActivity());
        viewPager.setAdapter(pagerAdapter);
    }

    public void attachDatabaseReadListener(){
        if(childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Toko toko = dataSnapshot.getValue(Toko.class);
                    Log.d("Key", dataSnapshot.getKey());
                    listToko.add(toko);
                    listToko.add(toko);
                    listKey.add(dataSnapshot.getKey());
                    listKey.add(dataSnapshot.getKey());
                    tokoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            tokoDatabaseReference.addChildEventListener(childEventListener);
        }
    }
}
