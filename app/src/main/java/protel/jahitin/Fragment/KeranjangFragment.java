package protel.jahitin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.Activity.Bayar;
import protel.jahitin.Adapter.KeranjangAdapter;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeranjangFragment extends Fragment
    implements KeranjangAdapter.KeranjangClickListener {

    private KeranjangAdapter adapter;
    private List<Pakaian> listPakaianDipilih = new ArrayList<>();
    private Button btnSubmit;
    private View emptyView, currentView;

    public KeranjangFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keranjang, container, false);

        Toolbar toolbar = view.findViewById(R.id.keranjang_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        btnSubmit = view.findViewById(R.id.btn_keranjang_submit);
        btnSubmit.setOnClickListener(onSubmitClickListener);
        emptyView = view.findViewById(R.id.empty_view);
        currentView = view.findViewById(R.id.root_view);

        RecyclerView recyclerView = view.findViewById(R.id.rv_keranjang);
        adapter = new KeranjangAdapter(getActivity(), listPakaianDipilih, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        prepareDummyList();

        checkEmpty();

        return view;
    }

    @Override
    public void onHapusClick(int clickedItemIndex) {

    }

    @Override
    public void onMinusClick(int clickedItemIndex, View view) {

    }

    @Override
    public void onPlusClick(int clickedItemIndex, View view) {

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

    public void prepareDummyList(){
        int[] images = new int[]{
                R.drawable.gambar_1,
                R.drawable.gambar_4
        };

        Pakaian p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
        listPakaianDipilih.add(p);
        p = new Pakaian("Kaos Santai", "Katun", "All Size",
                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
        listPakaianDipilih.add(p);
        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
        listPakaianDipilih.add(p);
        p = new Pakaian("Kaos Santai", "Katun", "All Size",
                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
        listPakaianDipilih.add(p);
        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
        listPakaianDipilih.add(p);
        p = new Pakaian("Kaos Santai", "Katun", "All Size",
                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
        listPakaianDipilih.add(p);

        adapter.notifyDataSetChanged();
    }

    public View.OnClickListener onSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), Bayar.class);
            startActivity(intent);
        }
    };
}
