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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import protel.jahitin.Adapter.DetailTransaksiPenjualanAdapter;
import protel.jahitin.Fragment.TransaksiPenjualanFragment;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

public class DetailTransaksiPenjualan extends AppCompatActivity{
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

    private TextView tanggalTV, caraPembayaranTV, kurirTV, alamatTV,
            hargaBarangTV, hargaKurirTv, totalHargaTV;
    private EditText noResiET;
    private RecyclerView recyclerView;
    private LinearLayout rootView;
    private Button btnSelesai;
    private Spinner statusSP;
    private ProgressBar loadingIndicator;
    private ProgressBarUtils pbUtils;

    private DetailTransaksiPenjualanAdapter adapter;

    private String status;
    private List<String> listStatus;

    public static final String DETAILTRANSAKSI_EXTRA = "detail_transaksi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_detail_transaksi_penjualan);
        super.onCreate(savedInstanceState);

        setToolbar();
        initUIComponent();
        initRecyclerView();
        pbUtils = new ProgressBarUtils();

        listStatus = Arrays.asList(getResources().getStringArray(R.array.status_transaksi_array));

        intent = getIntent();
        if(intent.hasExtra(TransaksiPenjualanFragment.EXTRA_DETAIL_TRANSAKSI_PENJUALAN)){
            keyTransaksi = intent.getStringExtra(TransaksiPenjualanFragment.EXTRA_DETAIL_TRANSAKSI_PENJUALAN);
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(DetailTransaksi.class.getSimpleName(), keyTransaksi);
        transaksi = new Transaksi();
        transaksiRef = FirebaseDatabase.getInstance().getReference().child("transaksi_penjualan")
                .child(mUser.getUid()).child(keyTransaksi);

        pakaianRef = FirebaseDatabase.getInstance().getReference().child("pakaian");

        attachDatabaseReadListener();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.dtransaksi_toolbar_penjualan);
        toolbar.setTitle("Detail Transaksi");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noResiET.getText().toString().trim().isEmpty()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("noResi", noResiET.getText().toString().trim());
                    transaksiRef.updateChildren(map);
                }
                finish();
            }
        });
    }

    public void attachDatabaseReadListener(){
        if(transaksiValue == null){
            pbUtils.showLoadingIndicator(loadingIndicator);
            rootView.setVisibility(View.GONE);

            transaksiValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    transaksi = dataSnapshot.getValue(Transaksi.class);
                    //statusSP.setText(transaksi.getStatus());
                    status = transaksi.getStatus();
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

                    statusSP.setSelection(listStatus.indexOf(status));

                    if(dataSnapshot.hasChild("noResi")){
                        String noResi = transaksi.getNoResi();
                        if(!noResi.isEmpty()){
                            noResiET.setText(noResi);
                        }
                    }
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
        tanggalTV = findViewById(R.id.detail_tanggal_pemesanan_penjualan);
        caraPembayaranTV = findViewById(R.id.detail_cara_pembayaran_penjualan);
        kurirTV = findViewById(R.id.detail_kurir_pengiriman_penjualan);
        alamatTV = findViewById(R.id.detail_alamat_pengiriman_penjualan);

        hargaBarangTV = findViewById(R.id.total_harga_barang_penjualan);
        hargaKurirTv = findViewById(R.id.harga_kurir_penjualan);
        totalHargaTV = findViewById(R.id.total_harga_penjualan);

        rootView = findViewById(R.id.root_view_detail_transaksi_penjualan);
        loadingIndicator = findViewById(R.id.pb_detail_transaksi_penjualan);

        noResiET = findViewById(R.id.et_noresi_transaksi);

        statusSP = findViewById(R.id.sp_status_transaksi);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.status_transaksi_array, android.R.layout.simple_spinner_dropdown_item
        );
        statusSP.setAdapter(spinnerAdapter);
        statusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = adapterView.getItemAtPosition(i).toString();

                Map<String, Object> map = new HashMap<>();
                map.put("status", status);
                transaksiRef.updateChildren(map);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public String getStringTanggal(long currentTime){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        Calendar waktuPesan = Calendar.getInstance();
        waktuPesan.setTimeInMillis(currentTime);

        String tanggal = formatter.format(waktuPesan.getTime());

        return tanggal;
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rv_detail_penjualan);
        adapter = new DetailTransaksiPenjualanAdapter(this, listPakaian, listJumlah);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!noResiET.getText().toString().trim().isEmpty()){
            Map<String, Object> map = new HashMap<>();
            map.put("noResi", noResiET.getText().toString().trim());
            transaksiRef.updateChildren(map);
        }
        finish();
    }
}
