package protel.jahitin.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.Adapter.TransaksiPembelianAdapter;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiPembelianFragment extends Fragment
    implements TransaksiPembelianAdapter.TransaksiPembelianClickListener {
    private View view;
    private RecyclerView recyclerView;
    private RelativeLayout emptyView;
    private TransaksiPembelianAdapter adapter;
    private List<Transaksi> listTransaksi = new ArrayList<>();
    private List<String> listTransaksiKey = new ArrayList<>();

    private DatabaseReference transaksiDatabaseReference;
    private ChildEventListener childEventListener;

    public TransaksiPembelianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaksi_pembelian, container, false);
        emptyView = view.findViewById(R.id.empty_view_transaksi);

        transaksiDatabaseReference = FirebaseDatabase.getInstance().getReference().child("transaksi").child("testuser");

        initRecyclerView();

        attachDatabaseReadListener();

        return view;
    }

    public void initRecyclerView(){
        recyclerView = view.findViewById(R.id.rv_transaksi_pembelian);
        adapter = new TransaksiPembelianAdapter(getActivity(), listTransaksi, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClick(int clickedItemIndex) {

    }

    public void attachDatabaseReadListener(){
        if(childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Transaksi transaksi = dataSnapshot.getValue(Transaksi.class);
                    listTransaksi.add(transaksi);
                    listTransaksiKey.add(dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();

                    checkListEmpty();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    checkListEmpty();
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

            transaksiDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    public void detachDatabaseReadListener(){
        if(childEventListener != null){
            transaksiDatabaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
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
