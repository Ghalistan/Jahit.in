package protel.jahitin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import protel.jahitin.Activity.Bayar;
import protel.jahitin.Adapter.KeranjangAdapter;
import protel.jahitin.Model.Keranjang;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeranjangFragment extends Fragment
    implements KeranjangAdapter.KeranjangClickListener {

    private KeranjangAdapter adapter;
    private List<Pakaian> listPakaianDipilih = new ArrayList<>();
    private List<Keranjang> listKeranjang = new ArrayList<>();
    private List<String> listPakaianKey = new ArrayList<>();
    private List<String> listKeranjangKey = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private Button btnSubmit;
    private View emptyView, currentView;
    private RecyclerView recyclerView;
    private View view;

    private FirebaseDatabase firebaseDatabase;
    private ChildEventListener keranjangEventListener, pakaianChildEventListener;
    private DatabaseReference keranjangDatabaseReference, pakaianDatabaseReference;

    public KeranjangFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_keranjang, container, false);

        Toolbar toolbar = view.findViewById(R.id.keranjang_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        btnSubmit = view.findViewById(R.id.btn_keranjang_submit);
        btnSubmit.setOnClickListener(onSubmitClickListener);
        emptyView = view.findViewById(R.id.empty_view);
        currentView = view.findViewById(R.id.root_view);

        initRecyclerView();

        firebaseDatabase = FirebaseDatabase.getInstance();
        keranjangDatabaseReference = firebaseDatabase.getReference().child("keranjang").child("testuser");
        pakaianDatabaseReference = firebaseDatabase.getReference().child("pakaian");

        attachDatabaseReadListener();

        //prepareDummyList();

        return view;
    }

    @Override
    public void onHapusClick(int clickedItemIndex) {
        listKeranjang.remove(clickedItemIndex);
        listPakaianDipilih.remove(clickedItemIndex);
        keranjangDatabaseReference
                .child(listKeranjangKey.get(clickedItemIndex))
                .removeValue();
        listKeranjangKey.remove(clickedItemIndex);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusClick(int clickedItemIndex, int jumlahBaru) {
        String keranjangKey = listKeranjangKey.get(clickedItemIndex);
        Keranjang keranjang =  listKeranjang.get(clickedItemIndex);
        keranjang.setJumlah(jumlahBaru);
        listKeranjang.set(clickedItemIndex, keranjang);
        map.clear();
        map.put(keranjangKey, listKeranjang.get(clickedItemIndex));
        keranjangDatabaseReference.updateChildren(map);
    }

    @Override
    public void onPlusClick(int clickedItemIndex, int jumlahBaru) {
        String keranjangKey = listKeranjangKey.get(clickedItemIndex);
        Keranjang keranjang =  listKeranjang.get(clickedItemIndex);
        keranjang.setJumlah(jumlahBaru);
        listKeranjang.set(clickedItemIndex, keranjang);
        map.clear();
        map.put(keranjangKey, listKeranjang.get(clickedItemIndex));
        keranjangDatabaseReference.updateChildren(map);
    }

    public void checkEmpty(){
        if(adapter != null){
            if(adapter.getItemCount() == 0){
                emptyView.setVisibility(View.VISIBLE);
                currentView.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
            }else{
                emptyView.setVisibility(View.GONE);
                currentView.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        }
    }

    //public void prepareDummyList(){
//        int[] images = new int[]{
//                R.drawable.gambar_1,
//                R.drawable.gambar_4
//        };
//
//        Pakaian p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
//                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
//        listPakaianDipilih.add(p);
//        p = new Pakaian("Kaos Santai", "Katun", "All Size",
//                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
//        listPakaianDipilih.add(p);
//        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
//                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
//        listPakaianDipilih.add(p);
//        p = new Pakaian("Kaos Santai", "Katun", "All Size",
//                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
//        listPakaianDipilih.add(p);
//        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
//                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
//        listPakaianDipilih.add(p);
//        p = new Pakaian("Kaos Santai", "Katun", "All Size",
//                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
//        listPakaianDipilih.add(p);
//
//        adapter.notifyDataSetChanged();}

    public View.OnClickListener onSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), Bayar.class);
            startActivity(intent);
        }
    };

    public void attachDatabaseReadListener(){
        if(keranjangEventListener == null){
            keranjangEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Keranjang k = dataSnapshot.getValue(Keranjang.class);
                    listKeranjang.add(k);
                    listPakaianKey.add(k.getIdBarang());
                    listKeranjangKey.add(dataSnapshot.getKey());
                    Log.d("here", k.getIdBarang());

                    checkEmpty();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    checkEmpty();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    checkEmpty();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            keranjangDatabaseReference.addChildEventListener(keranjangEventListener);
        }

        if(pakaianChildEventListener == null){
            pakaianChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Pakaian p = dataSnapshot.getValue(Pakaian.class);
                    if(listPakaianKey.contains(dataSnapshot.getKey())) {
                        listPakaianDipilih.add(p);
                        Log.d("Here", p.getNama());
                    }
                    adapter.notifyDataSetChanged();
                    checkEmpty();
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

            pakaianDatabaseReference.addChildEventListener(pakaianChildEventListener);
        }
    }


    public void initRecyclerView(){
        recyclerView = view.findViewById(R.id.rv_keranjang);
        adapter = new KeranjangAdapter(getActivity(), listPakaianDipilih, listKeranjang, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
