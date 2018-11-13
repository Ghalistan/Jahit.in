package protel.jahitin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import protel.jahitin.Adapter.DetailTransaksiAdapter;
import protel.jahitin.Fragment.TransaksiPembelianFragment;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

public class DetailTransaksi extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    private DatabaseReference transaksiRef, pakaianRef;
    private ValueEventListener transaksiValue, pakaianValue;
    private List<Pakaian> listPakaian = new ArrayList<>();
    private List<Object> listAllJumlah = new ArrayList<>();
    private List<Object> listJumlah = new ArrayList<>();
    private FirebaseUser mUser;

    private Intent intent;
    private String keyTransaksi = "", keyToko = "";
    private Transaksi transaksi;

    private TextView statusTV, tanggalTV, caraPembayaranTV, kurirTV, alamatTV,
                      hargaBarangTV, hargaKurirTv, totalHargaTV;
    private RecyclerView recyclerView;
    private LinearLayout rootView;
    private Button btnSelesai;
    private ProgressBar loadingIndicator;
    private ProgressBarUtils pbUtils;

    private DetailTransaksiAdapter adapter;

    public static final String DETAILTRANSAKSI_EXTRA = "detail_transaksi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_detail_transaksi);
        super.onCreate(savedInstanceState);

        setToolbar();
        initUIComponent();
        initRecyclerView();
        pbUtils = new ProgressBarUtils();

        intent = getIntent();
        if(intent.hasExtra(TransaksiPembelianFragment.EXTRA_DETAIL_TRANSAKSI)){
            keyTransaksi = intent.getStringExtra(TransaksiPembelianFragment.EXTRA_DETAIL_TRANSAKSI);
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(DetailTransaksi.class.getSimpleName(), keyTransaksi);
        transaksi = new Transaksi();
        transaksiRef = FirebaseDatabase.getInstance().getReference().child("transaksi")
                .child(mUser.getUid()).child(keyTransaksi);

        pakaianRef = FirebaseDatabase.getInstance().getReference().child("pakaian");

        attachDatabaseReadListener();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.dtransaksi_toolbar);
        toolbar.setTitle("Detail Transaksi");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_selesai_detail_transaksi:
                Intent intent = new Intent(getApplicationContext(), Ulasan.class);
                intent.putExtra(DETAILTRANSAKSI_EXTRA, keyTransaksi);
                startActivity(intent);
                break;

        }
    }

    public void attachDatabaseReadListener(){
        if(transaksiValue == null){
            pbUtils.showLoadingIndicator(loadingIndicator);
            rootView.setVisibility(View.GONE);

            transaksiValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    transaksi = dataSnapshot.getValue(Transaksi.class);
                    statusTV.setText(transaksi.getStatus());
                    caraPembayaranTV.setText("Transfer" + transaksi.getCaraBayar());
                    kurirTV.setText(transaksi.getKurir());
                    alamatTV.setText(transaksi.getAlamat());

                    hargaBarangTV.setText(String.valueOf(transaksi.getHargaBarang()));
                    hargaKurirTv.setText(String.valueOf(transaksi.getHargaKurir()));
                    totalHargaTV.setText(String.valueOf(transaksi.getTotalHarga() ));

                    String tanggal = getStringTanggal(transaksi.getWaktuTransaksi());
                    tanggalTV.setText(tanggal);

                    listAllJumlah = transaksi.getJumlah();

                    List<Object> listKeyPakaian = transaksi.getBarang();
                    for(int i = 0; i < listKeyPakaian.size(); i++){
                        pakaianRef.child(listKeyPakaian.get(i).toString())
                                .addListenerForSingleValueEvent(
                                        createValueListener((Long) listAllJumlah.get(i)));
                    }

                    pbUtils.hideLoadingIndicator(loadingIndicator);
                    rootView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pbUtils.hideLoadingIndicator(loadingIndicator);
                    rootView.setVisibility(View.VISIBLE);
                }
            };

            transaksiRef.addValueEventListener(transaksiValue);
        }
    }

    public void detachDatabaseListener(){
        if(transaksiValue != null){
            transaksiRef.removeEventListener(transaksiValue);
            transaksiValue = null;
        }

        if(pakaianValue != null){
            pakaianRef.removeEventListener(pakaianValue);
            pakaianValue = null;
        }

        listPakaian.clear();
        listJumlah.clear();
        listAllJumlah.clear();
    }

    public ValueEventListener createValueListener(final long jumlahBarang){
        pakaianValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pakaian p = dataSnapshot.getValue(Pakaian.class);
                listPakaian.add(p);
                // this method to get the index is retarded
                listJumlah.add(jumlahBarang);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return pakaianValue;
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    public void initUIComponent(){
        statusTV = findViewById(R.id.detail_status);
        tanggalTV = findViewById(R.id.detail_tanggal_pemesanan);
        caraPembayaranTV = findViewById(R.id.detail_cara_pembayaran);
        kurirTV = findViewById(R.id.detail_kurir_pengiriman);
        alamatTV = findViewById(R.id.detail_alamat_pengiriman);

        hargaBarangTV = findViewById(R.id.total_harga_barang);
        hargaKurirTv = findViewById(R.id.harga_kurir);
        totalHargaTV = findViewById(R.id.total_harga);

        btnSelesai = findViewById(R.id.btn_selesai_detail_transaksi);
        btnSelesai.setOnClickListener(this);

        rootView = findViewById(R.id.root_view_detail_transaksi);
        loadingIndicator = findViewById(R.id.pb_detail_transaksi);
    }

    public String getStringTanggal(long currentTime){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        Calendar waktuPesan = Calendar.getInstance();
        waktuPesan.setTimeInMillis(currentTime);

        String tanggal = formatter.format(waktuPesan.getTime());

        return tanggal;
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rv_detail);
        adapter = new DetailTransaksiAdapter(this, listPakaian, listJumlah);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
