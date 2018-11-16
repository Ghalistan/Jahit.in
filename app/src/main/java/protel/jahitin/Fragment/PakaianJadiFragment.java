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
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import protel.jahitin.Activity.InformasiBarang;
import protel.jahitin.Adapter.PakaianJadiAdapter;
import protel.jahitin.Model.Keranjang;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianJadiFragment extends Fragment
        implements View.OnClickListener, PakaianJadiAdapter.PakaianJadiClickListener {
    private RecyclerView pakaianJadiRecyclerView;
    private PakaianJadiAdapter adapter;
    private View view;
    private List<Pakaian> listPakaianDipilih = new ArrayList<>();
    private List<Boolean> listBeli = new ArrayList<>();
    private List<String> listKeyPakaian = new ArrayList<>();
    private List<String> listKey = new ArrayList<>();
    private List<Keranjang> listKeranjang = new ArrayList<>();
    private String keyToko;

    private ProgressBar progressBar;
    private ProgressBarUtils pbUtils;

    public static int EXTRA_PAKAIAN_JADI_FRAGMENT = 1;
    public static String EXTRA_INFORMASI_BARANG = "key_barang";

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser mUser;
    private DatabaseReference pakaianDariTokoDipilihReference, keranjangDatabaseReference, pakaianDatabaseReference;
    private ChildEventListener pakaianEventListener;
    private ValueEventListener tokoValueListener, keranjangValueListener;

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

        progressBar = view.findViewById(R.id.pb_pakaian_jadi);
        pbUtils = new ProgressBarUtils();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        keranjangDatabaseReference = firebaseDatabase.getReference().child("keranjang").child(mUser.getUid());
        if (!keyToko.isEmpty()) {
            pakaianDariTokoDipilihReference = firebaseDatabase.getReference().child("toko").child(keyToko).child("pakaian");
        }
        pakaianDatabaseReference = firebaseDatabase.getReference().child("pakaian");

        attachDatabaseReadListener();

        Toolbar bottomToolbar = view.findViewById(R.id.bottom_toolbar_pembelian);

        Button btnCheckout = bottomToolbar.findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(this);

        initRecyclerView();
        //prepareDummyList();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        clearList();
    }

    public void clearList(){
        listPakaianDipilih.clear();
        listBeli.clear();
        listKeyPakaian.clear();
        listKey.clear();
        listKeranjang.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout:
                Map<String, Object> map = new HashMap<>();
                for(int i = 0; i < listBeli.size(); i++){
                    if(listBeli.get(i)){
                        Keranjang k = new Keranjang(listKey.get(i), keyToko, 1);
                        if(!listKeranjang.contains(k)){
                            map.put(k.getIdBarang(), k);
                        }
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
    public void onBarangItemClick(int clickedItemIndex) {
        Intent informasiBarangIntent = new Intent(getContext(), InformasiBarang.class);
        informasiBarangIntent.putExtra(EXTRA_INFORMASI_BARANG, listKey.get(clickedItemIndex));
        startActivity(informasiBarangIntent);
    }

//    @Override
//    public void onLikeItemClick(int clickedItemIndex, View view) {
//
//    }

    public void attachDatabaseReadListener(){
        if(tokoValueListener == null){
            pbUtils.showLoadingIndicator(progressBar);
            tokoValueListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        String keyPakaian = data.getKey();
                        Log.d(PakaianJadiFragment.class.getSimpleName(), keyPakaian);
                        listKeyPakaian.add(keyPakaian);
                        listBeli.add(false);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            pakaianDariTokoDipilihReference.addValueEventListener(tokoValueListener);
        }

        if(pakaianEventListener == null){
            pakaianEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Pakaian p = dataSnapshot.getValue(Pakaian.class);
                    if(listKeyPakaian.contains(dataSnapshot.getKey())){
                        listPakaianDipilih.add(p);
                        adapter.notifyDataSetChanged();
                        listBeli.add(false);
                        listKey.add(dataSnapshot.getKey());
                    }
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

            pakaianDatabaseReference.addChildEventListener(pakaianEventListener);
        }

        if(keranjangValueListener == null){
            keranjangValueListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Keranjang k = data.getValue(Keranjang.class);
                        listKeranjang.add(k);
                    }

                    pbUtils.hideLoadingIndicator(progressBar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pbUtils.hideLoadingIndicator(progressBar);
                }
            };

            keranjangDatabaseReference.addValueEventListener(keranjangValueListener);
        }
    }

    public void detachDatabaseReadListener(){
        if(tokoValueListener != null){
            pakaianDariTokoDipilihReference.removeEventListener(tokoValueListener);
            tokoValueListener = null;
        }

        if(pakaianEventListener != null){
            pakaianDatabaseReference.removeEventListener(pakaianEventListener);
            pakaianEventListener = null;
        }

        if(keranjangValueListener != null){
            keranjangDatabaseReference.removeEventListener(keranjangValueListener);
            keranjangValueListener = null;
        }
    }

    public void initRecyclerView(){
        pakaianJadiRecyclerView = view.findViewById(R.id.rv_pakaian_jadi);
        adapter = new PakaianJadiAdapter(getActivity(), listPakaianDipilih, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        pakaianJadiRecyclerView.setLayoutManager(layoutManager);
        pakaianJadiRecyclerView.setAdapter(adapter);
    }

    public void tambahItem(int clickedItemIndex, View view){
        listBeli.set(clickedItemIndex, true);


        Button btnTambah = view.findViewById(R.id.btn_tambah_pakaian_jadi);
        btnTambah.setBackgroundResource(R.drawable.button_tambah_active);
        btnTambah.setTextColor(getResources().getColor(R.color.colorAccent, null));
        btnTambah.setText(R.string.button_tambah_batal);
    }

    public void hapusItem(int clickedItemIndex, View view){
        listBeli.set(clickedItemIndex, false);

        Button btnTambah = view.findViewById(R.id.btn_tambah_pakaian_jadi);
        btnTambah.setBackgroundResource(R.drawable.button_tambah_not_active);
        btnTambah.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        btnTambah.setText(R.string.button_tambah_default);
    }
}
