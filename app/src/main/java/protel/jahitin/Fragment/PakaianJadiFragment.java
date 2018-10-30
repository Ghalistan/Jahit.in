package protel.jahitin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.Activity.Beranda;
import protel.jahitin.Adapter.PakaianJadiAdapter;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianJadiFragment extends Fragment
        implements View.OnClickListener, PakaianJadiAdapter.PakaianJadiClickListener {
    private RecyclerView pakaianJadiRecyclerView;
    private PakaianJadiAdapter adapter;
    private List<Pakaian> listPakaian = new ArrayList<>();
    private List<Boolean> listBeli = new ArrayList<>();

    public static int EXTRA_PAKAIAN_JADI_FRAGMENT = 1;

    public PakaianJadiFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pakaian_jadi, container, false);
        // Inflate the layout for this fragment

        Toolbar bottomToolbar = view.findViewById(R.id.bottom_toolbar_pembelian);

        Button btnCheckout = bottomToolbar.findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(this);

        pakaianJadiRecyclerView = view.findViewById(R.id.rv_pakaian_jadi);
        adapter = new PakaianJadiAdapter(getActivity(), listPakaian, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        pakaianJadiRecyclerView.setLayoutManager(layoutManager);
        pakaianJadiRecyclerView.setAdapter(adapter);
        prepareDummyList();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout:
                Intent intent = new Intent(getActivity(), Beranda.class);
                intent.putExtra(Intent.EXTRA_TEXT, EXTRA_PAKAIAN_JADI_FRAGMENT);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void onTambahItemClick(int clickedItemIndex, View view) {
        if(listBeli.get(clickedItemIndex)){
            listBeli.set(clickedItemIndex, false);
            Button btnTambah =view.findViewById(R.id.btn_tambah_pakaian_jadi);
            btnTambah.setBackgroundResource(R.drawable.button_tambah_not_active);
            btnTambah.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            btnTambah.setText(R.string.button_tambah_default);
        }else{
            listBeli.set(clickedItemIndex, true);
            Button btnTambah = view.findViewById(R.id.btn_tambah_pakaian_jadi);
            btnTambah.setBackgroundResource(R.drawable.button_tambah_active);
            btnTambah.setTextColor(getResources().getColor(R.color.colorAccent, null));
            btnTambah.setText(R.string.button_tambah_batal);
        }
    }

    @Override
    public void onLikeItemClick(int clickedItemIndex, View view) {

    }

    public void prepareDummyList(){
        int[] images = new int[]{
                R.drawable.gambar_1,
                R.drawable.gambar_4
        };

        Pakaian p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
        listPakaian.add(p);
        p = new Pakaian("Kaos Santai", "Katun", "All Size",
                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
        listPakaian.add(p);
        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
        listPakaian.add(p);
        p = new Pakaian("Kaos Santai", "Katun", "All Size",
                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
        listPakaian.add(p);
        p = new Pakaian("Kemeja Motif Kotak", "Katun", "All Size",
                "Kemeja", "Kuning", 120000, images[0], Pakaian.GENDER_PRIA);
        listPakaian.add(p);
        p = new Pakaian("Kaos Santai", "Katun", "All Size",
                "Kaos", "Kuning", 60000, images[1], Pakaian.GENDER_WANITA);
        listPakaian.add(p);


        for(int i = 0; i < listPakaian.size(); i++)
            listBeli.add(false);

        adapter.notifyDataSetChanged();
    }
}
