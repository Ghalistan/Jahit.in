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
import android.widget.Toast;

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
    private TextView totalHargaTextView;
    private int totalHarga;

    private FirebaseDatabase firebaseDatabase;
    private ChildEventListener keranjangEventListener, pakaianChildEventListener;
    private ValueEventListener pakaianValueEventListener;
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

        totalHargaTextView = view.findViewById(R.id.tv_total_harga_keranjang);

        initRecyclerView();

        firebaseDatabase = FirebaseDatabase.getInstance();
        keranjangDatabaseReference = firebaseDatabase.getReference().child("keranjang").child("testuser");
        pakaianDatabaseReference = firebaseDatabase.getReference().child("pakaian");

        attachDatabaseReadListener();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    @Override
    public void onHapusClick(int clickedItemIndex) {
        totalHarga -= (listKeranjang.get(clickedItemIndex).getJumlah() *
                        listPakaianDipilih.get(clickedItemIndex).getHarga());
        totalHargaTextView.setText(String.valueOf(totalHarga));

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
        kurangiHarga(clickedItemIndex);

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
        tambahHarga(clickedItemIndex);

        String keranjangKey = listKeranjangKey.get(clickedItemIndex);
        Keranjang keranjang =  listKeranjang.get(clickedItemIndex);
        keranjang.setJumlah(jumlahBaru);
        listKeranjang.set(clickedItemIndex, keranjang);
        map.clear();
        map.put(keranjangKey, listKeranjang.get(clickedItemIndex));
        keranjangDatabaseReference.updateChildren(map);
    }

    public void kurangiHarga(int clickedItemIndex){
        totalHarga -= listPakaianDipilih.get(clickedItemIndex).getHarga();
        totalHargaTextView.setText("Rp. " + String.valueOf(totalHarga));
    }

    public void tambahHarga(int clickedItemIndex){
        totalHarga += listPakaianDipilih.get(clickedItemIndex).getHarga();
        totalHargaTextView.setText("Rp. " + String.valueOf(totalHarga));
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
                //
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
                        adapter.notifyDataSetChanged();
                    }
                    totalHargaTextView.setText("Rp. "+ String.valueOf(totalHarga));
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

        if(pakaianValueEventListener == null){
            pakaianValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(int i = 0; i < listPakaianDipilih.size(); i++){
                        int jumlah = listKeranjang.get(i).getJumlah();
                        int harga = listPakaianDipilih.get(i).getHarga();
                        totalHarga += harga * jumlah;
                    }

                    String stringTotalHarga = "Rp. " + totalHarga;
                    totalHargaTextView.setText(stringTotalHarga);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            pakaianDatabaseReference.addValueEventListener(pakaianValueEventListener);
        }
    }

    public void detachDatabaseReadListener(){
        if(keranjangEventListener != null){
            keranjangDatabaseReference.removeEventListener(keranjangEventListener);
            keranjangEventListener = null;
        }

        if(pakaianChildEventListener != null){
            pakaianDatabaseReference.removeEventListener(pakaianChildEventListener);
            pakaianChildEventListener = null;
        }

        if(pakaianValueEventListener != null){
            pakaianDatabaseReference.removeEventListener(pakaianValueEventListener);
            pakaianValueEventListener = null;
        }
    }

    public void initRecyclerView(){
        recyclerView = view.findViewById(R.id.rv_keranjang);
        adapter = new KeranjangAdapter(getActivity(), listPakaianDipilih, listKeranjang, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public View.OnClickListener onSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!listPakaianDipilih.isEmpty()) {
                Intent intent = new Intent(getActivity(), Bayar.class);
                startActivity(intent);
            }else{
                Toast toast = Toast.makeText(getContext(),"Pilih barang terlebih dahulu", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };
}
