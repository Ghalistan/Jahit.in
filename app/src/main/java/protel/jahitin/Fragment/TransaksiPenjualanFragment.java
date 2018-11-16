package protel.jahitin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.Activity.DetailTransaksi;
import protel.jahitin.Activity.DetailTransaksiPenjualan;
import protel.jahitin.Adapter.TransaksiPembelianAdapter;
import protel.jahitin.Adapter.TransaksiPenjualanAdapter;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiPenjualanFragment extends Fragment
        implements TransaksiPenjualanAdapter.TransaksiPenjualanClickListener {

    private View view;
    private RecyclerView recyclerView;
    private RelativeLayout emptyView;
    private TransaksiPenjualanAdapter adapter;
    private List<Transaksi> listTransaksi = new ArrayList<>();
    private List<String> listTransaksiKey = new ArrayList<>();

    private DatabaseReference transaksiDatabaseReference;
    private ValueEventListener transaksiValueListener;
    private FirebaseUser mUser;

    private ProgressBar progressBar;
    private ProgressBarUtils pbUtils;

    public static final String EXTRA_DETAIL_TRANSAKSI_PENJUALAN = "detail_transaksi_penjualan";

    public TransaksiPenjualanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaksi_penjualan, container, false);
        emptyView = view.findViewById(R.id.empty_view_transaksi_penjualan);
        progressBar = view.findViewById(R.id.pb_transaksi_penjualan);

        pbUtils = new ProgressBarUtils();

        initRecyclerView();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        transaksiDatabaseReference = FirebaseDatabase.getInstance().getReference().child("transaksi_penjualan").child(mUser.getUid());

        attachDatabaseReadListener();

        return view;
    }

    public void initRecyclerView(){
        recyclerView = view.findViewById(R.id.rv_transaksi_penjualan);
        adapter = new TransaksiPenjualanAdapter(getActivity(), listTransaksi, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        Intent intentDetailTransaksi = new Intent(getActivity(), DetailTransaksiPenjualan.class);
        intentDetailTransaksi.putExtra(EXTRA_DETAIL_TRANSAKSI_PENJUALAN, listTransaksiKey.get(clickedItemIndex));
        startActivity(intentDetailTransaksi);
    }

    public void attachDatabaseReadListener(){
        if(transaksiValueListener == null){
            pbUtils.showLoadingIndicator(progressBar);
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);

            transaksiValueListener =
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                Transaksi transaksi = data.getValue(Transaksi.class);
                                listTransaksi.add(transaksi);
                                listTransaksiKey.add(data.getKey());
                                adapter.notifyDataSetChanged();
                            }

                            pbUtils.hideLoadingIndicator(progressBar);
                            checkListEmpty();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            pbUtils.hideLoadingIndicator(progressBar);
                        }
                    };

            transaksiDatabaseReference.addValueEventListener(transaksiValueListener);
        }
    }

    public void detachDatabaseReadListener(){
        if(transaksiValueListener != null){
            transaksiDatabaseReference.removeEventListener(transaksiValueListener);
            transaksiValueListener = null;
            Log.d("TRANSAKSI", "Value Removed");
        }

        listTransaksi.clear();
        listTransaksiKey.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TRANSAKSI", "onPause");
        detachDatabaseReadListener();
    }

    @Override
    public void onStart() {
        Log.d("TRANSAKSI", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TRANSAKSI", "onResume");
        attachDatabaseReadListener();
    }

    public void checkListEmpty(){
        if(listTransaksi.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
