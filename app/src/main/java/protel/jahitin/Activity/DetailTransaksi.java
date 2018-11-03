package protel.jahitin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import protel.jahitin.Fragment.TransaksiPembelianFragment;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;

public class DetailTransaksi extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    private DatabaseReference transaksiRef, pakaianRef;
    private ValueEventListener transaksiValue, pakaianValue;
    private List<Pakaian> listPakaian = new ArrayList<>();

    private Intent intent;
    private String keyTransaksi = "";
    private Transaksi transaksi;

    private TextView statusTV, tanggalTV, caraPembayaranTV, kurirTV, alamatTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_detail_transaksi);
        super.onCreate(savedInstanceState);

        setToolbar();
        initUIComponent();

        intent = getIntent();
        if(intent.hasExtra(TransaksiPembelianFragment.EXTRA_DETAIL_TRANSAKSI)){
            keyTransaksi = intent.getStringExtra(TransaksiPembelianFragment.EXTRA_DETAIL_TRANSAKSI);
        }

        transaksi = new Transaksi();
        transaksiRef = FirebaseDatabase.getInstance().getReference().child("transaksi").child(keyTransaksi);
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

    }

    public void attachDatabaseReadListener(){
        if(transaksiValue == null){
            transaksiValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    transaksi = dataSnapshot.getValue(Transaksi.class);
                    statusTV.setText(transaksi.getStatus());
                    caraPembayaranTV.setText("Transfer" + transaksi.getCaraBayar());
                    kurirTV.setText(transaksi.getKurir());
                    alamatTV.setText(transaksi.getAlamat());

                    String tanggal = "";
                    try {
                        tanggal = getStringTanggal(transaksi.getWaktuTransaksi());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(!tanggal.isEmpty()){
                        tanggalTV.setText(tanggal);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
    }

    //statusTV, tanggalTV, caraPembayaranTV, kurirTV, alamatTV

    public void initUIComponent(){
        statusTV = findViewById(R.id.detail_status);
        tanggalTV = findViewById(R.id.detail_tanggal_pemesanan);
        caraPembayaranTV = findViewById(R.id.detail_cara_pembayaran);
        kurirTV = findViewById(R.id.detail_kurir_pengiriman);
        alamatTV = findViewById(R.id.detail_alamat_pengiriman);
    }

    public String getStringTanggal(long currentTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        Calendar waktuPesan = Calendar.getInstance();
        waktuPesan.setTimeInMillis(currentTime);

        String tanggal = formatter.format(waktuPesan.getTime());

        return tanggal;
    }
}
