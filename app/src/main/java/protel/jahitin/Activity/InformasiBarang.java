package protel.jahitin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import protel.jahitin.Fragment.PakaianJadiFragment;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;

public class InformasiBarang extends AppCompatActivity {

    private ImageView gambarBarang;
    private TextView namaTV, bahanTV, ukuranTV, hargaTV, catatanTV;
    private Toolbar toolbar;

    private DatabaseReference barangRef;
    private ValueEventListener barangVal;

    private String keyBarang;
    private Pakaian pakaian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_barang);

        toolbar = findViewById(R.id.informasi_barang_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        keyBarang = "";
        Intent intentTrigger = getIntent();
        if(intentTrigger.hasExtra(PakaianJadiFragment.EXTRA_INFORMASI_BARANG)){
            keyBarang = intentTrigger.getStringExtra(PakaianJadiFragment.EXTRA_INFORMASI_BARANG);
        }

        barangRef = FirebaseDatabase.getInstance().getReference()
                .child("pakaian").child(keyBarang);

        initUIcomponent();

        attachDatabaseListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    private void initUIcomponent(){
        gambarBarang = findViewById(R.id.iv_informasi_barang);
        namaTV = findViewById(R.id.tv_nama_informasi_barang);
        bahanTV = findViewById(R.id.tv_bahan_informasi_barang);
        ukuranTV = findViewById(R.id.tv_ukuran_informasi_barang);
        hargaTV = findViewById(R.id.tv_harga_informasi_barang);
        catatanTV = findViewById(R.id.tv_catatan_informasi_barang);
    }

    private void attachDatabaseListener(){
        if(barangVal == null){
            barangVal = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pakaian = dataSnapshot.getValue(Pakaian.class);
                    Glide.with(getApplicationContext()).load(pakaian.getImageUrl())
                            .into(gambarBarang);

                    namaTV.setText(pakaian.getNama());
                    bahanTV.setText(pakaian.getBahan());
                    ukuranTV.setText(pakaian.getUkuranTersedia().get(0).toString());
                    String harga = "Rp " + String.valueOf(pakaian.getHarga());
                    hargaTV.setText(harga);
                    catatanTV.setText(pakaian.getCatatan());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            barangRef.addValueEventListener(barangVal);
        }
    }

    private void detachDatabaseListener(){
        if(barangVal != null){
            barangRef.removeEventListener(barangVal);
            barangVal = null;
        }
    }
}
