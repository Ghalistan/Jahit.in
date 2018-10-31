package protel.jahitin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import protel.jahitin.Activity.Beranda;
import protel.jahitin.Adapter.PakaianJadiAdapter;
import protel.jahitin.Model.Keranjang;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianJadiFragment extends Fragment
        implements View.OnClickListener, PakaianJadiAdapter.PakaianJadiClickListener {
    private RecyclerView pakaianJadiRecyclerView;
    private PakaianJadiAdapter adapter;
    private View view;
    private List<Pakaian> listPakaian = new ArrayList<>();
    private List<Boolean> listBeli = new ArrayList<>();
    private List<String> listKey = new ArrayList<>();
    private List<Keranjang> listKeranjang = new ArrayList<>();
    private List<Keranjang> listKeranjangDipilih = new ArrayList<>();
    private String keyToko;

    public static int EXTRA_PAKAIAN_JADI_FRAGMENT = 1;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tokoDipilihDatabaseReference, keranjangDatabaseReference;
    private ChildEventListener childEventListener, keranjangChildEventListener;

    public PakaianJadiFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pakaian_jadi, container, false);
        // Inflate the layout for this fragment

        Intent intentAsal = getActivity().getIntent();
        keyToko = "";
        if(intentAsal != null){
            if(intentAsal.hasExtra(BerandaFragment.EXTRA_KEY_TOKO)){
                Log.d(PakaianJadiFragment.class.getSimpleName(), intentAsal.getStringExtra(BerandaFragment.EXTRA_KEY_TOKO));
                keyToko = intentAsal.getStringExtra(BerandaFragment.EXTRA_KEY_TOKO);
            }
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        keranjangDatabaseReference = firebaseDatabase.getReference().child("keranjang").child("testuser");
        if (!keyToko.isEmpty()) {
            tokoDipilihDatabaseReference = firebaseDatabase.getReference().child("pakaian").child(keyToko);
            attachDatabaseReadListener();
        }


        Toolbar bottomToolbar = view.findViewById(R.id.bottom_toolbar_pembelian);

        Button btnCheckout = bottomToolbar.findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(this);

        initRecyclerView();
        //prepareDummyList();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout:
                Map<String, Object> map = new HashMap<>();
                for(Keranjang k : listKeranjangDipilih){
                    if(!listKeranjang.contains(k)){
                        map.put(k.getIdBarang(), k);
                    }
                }
                keranjangDatabaseReference.updateChildren(map);

                Intent intent = new Intent(getActivity(), Beranda.class);
                intent.putExtra(Intent.EXTRA_TEXT, EXTRA_PAKAIAN_JADI_FRAGMENT);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void onTambahItemClick(int clickedItemIndex, View view) {
        if(listBeli.get(clickedItemIndex)){
            hapusItem(clickedItemIndex, view);
        }else{
            tambahItem(clickedItemIndex, view);
        }
    }

    @Override
    public void onLikeItemClick(int clickedItemIndex, View view) {

    }

    //public void prepareDummyList(){
//        int[] images = new int[]{
//                R.drawable.gambar_1,
//                R.drawable.gambar_4
//        };
//
//        Pakaian p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
//                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
//        listPakaian.add(p);
//        p = new Pakaian("Kaos Santai", "Katun", "All Size",
//                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
//        listPakaian.add(p);
//        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
//                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
//        listPakaian.add(p);
//        p = new Pakaian("Kaos Santai", "Katun", "All Size",
//                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
//        listPakaian.add(p);
//        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
//                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
//        listPakaian.add(p);
//        p = new Pakaian("Kaos Santai", "Katun", "All Size",
//                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
//        listPakaian.add(p);
//
//
//        for(int i = 0; i < listPakaian.size(); i++)
//            listBeli.add(false);
//
//        adapter.notifyDataSetChanged();}

    public void attachDatabaseReadListener(){
        if(childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Pakaian pakaian = dataSnapshot.getValue(Pakaian.class);
                    Log.d(PakaianJadiFragment.class.getSimpleName(), dataSnapshot.getKey());
                    listPakaian.add(pakaian);
                    listKey.add(dataSnapshot.getKey());
                    listBeli.add(false);
                    adapter.notifyDataSetChanged();
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

            tokoDipilihDatabaseReference.addChildEventListener(childEventListener);
        }

        if(keranjangChildEventListener == null){
            keranjangChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Keranjang k = dataSnapshot.getValue(Keranjang.class);
                    listKeranjang.add(k);
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

            keranjangDatabaseReference.addChildEventListener(keranjangChildEventListener);
        }
    }

    public void initRecyclerView(){
        pakaianJadiRecyclerView = view.findViewById(R.id.rv_pakaian_jadi);
        adapter = new PakaianJadiAdapter(getActivity(), listPakaian, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        pakaianJadiRecyclerView.setLayoutManager(layoutManager);
        pakaianJadiRecyclerView.setAdapter(adapter);
    }

    public void tambahItem(int clickedItemIndex, View view){
        listBeli.set(clickedItemIndex, true);
        Keranjang keranjang = new Keranjang(listKey.get(clickedItemIndex), keyToko, 1);
        listKeranjangDipilih.add(keranjang);


        Button btnTambah = view.findViewById(R.id.btn_tambah_pakaian_jadi);
        btnTambah.setBackgroundResource(R.drawable.button_tambah_active);
        btnTambah.setTextColor(getResources().getColor(R.color.colorAccent, null));
        btnTambah.setText(R.string.button_tambah_batal);
    }

    public void hapusItem(int clickedItemIndex, View view){
        listBeli.set(clickedItemIndex, false);
        listKeranjangDipilih.remove(listKeranjang.get(clickedItemIndex));

        Button btnTambah = view.findViewById(R.id.btn_tambah_pakaian_jadi);
        btnTambah.setBackgroundResource(R.drawable.button_tambah_not_active);
        btnTambah.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        btnTambah.setText(R.string.button_tambah_default);
    }
}
