package protel.jahitin.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import protel.jahitin.Fragment.TransaksiPembelianFragment;
import protel.jahitin.R;

public class Ulasan extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView star0,star1,star2,star3,star4;
    Button btnKirim;
    int totalStar = 0;
    boolean cekStar = false;
    private String keyTransaksi;

    private DatabaseReference transaksiRef;
    private FirebaseUser mUser;

    private static final String BARANG_DITERIMA = "Barang Diterima";
    private static final String STATUS_KEY = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_ulasan);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.hasExtra(DetailTransaksi.DETAILTRANSAKSI_EXTRA)){
            keyTransaksi = intent.getStringExtra(DetailTransaksi.DETAILTRANSAKSI_EXTRA);
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        transaksiRef = FirebaseDatabase.getInstance().getReference().child("transaksi")
                .child(mUser.getUid()).child(keyTransaksi);

        toolbar = findViewById(R.id.ulasan_toolbar);
        toolbar.setTitle("Ulasan");
        setSupportActionBar(toolbar);

        star0 = findViewById(R.id.star_0);
        star1 = findViewById(R.id.star_1);
        star2 = findViewById(R.id.star_2);
        star3 = findViewById(R.id.star_3);
        star4 = findViewById(R.id.star_4);
        btnKirim = findViewById(R.id.button_kirim_ulasan);

        star0.setOnClickListener(this);
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        btnKirim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.star_0:
                if (!cekStar) {
                    star0.setImageResource(R.drawable.star2);
                    star1.setImageResource(R.drawable.star3);
                    star2.setImageResource(R.drawable.star3);
                    star3.setImageResource(R.drawable.star3);
                    star4.setImageResource(R.drawable.star3);
                    totalStar = 1;
                    cekStar= true;
                } else {
                    star0.setImageResource(R.drawable.star3);
                    star1.setImageResource(R.drawable.star3);
                    star2.setImageResource(R.drawable.star3);
                    star3.setImageResource(R.drawable.star3);
                    star4.setImageResource(R.drawable.star3);
                    totalStar = 0;
                    cekStar = false;
                }
                break;
            case R.id.star_1:
                star0.setImageResource(R.drawable.star2);
                star1.setImageResource(R.drawable.star2);
                star2.setImageResource(R.drawable.star3);
                star3.setImageResource(R.drawable.star3);
                star4.setImageResource(R.drawable.star3);
                totalStar = 2;
                break;
            case R.id.star_2:
                star0.setImageResource(R.drawable.star2);
                star1.setImageResource(R.drawable.star2);
                star2.setImageResource(R.drawable.star2);
                star3.setImageResource(R.drawable.star3);
                star4.setImageResource(R.drawable.star3);
                totalStar = 3;
                break;
            case R.id.star_3:
                star0.setImageResource(R.drawable.star2);
                star1.setImageResource(R.drawable.star2);
                star2.setImageResource(R.drawable.star2);
                star3.setImageResource(R.drawable.star2);
                star4.setImageResource(R.drawable.star3);
                totalStar = 4;
                break;
            case R.id.star_4:
                star0.setImageResource(R.drawable.star2);
                star1.setImageResource(R.drawable.star2);
                star2.setImageResource(R.drawable.star2);
                star3.setImageResource(R.drawable.star2);
                star4.setImageResource(R.drawable.star2);
                totalStar = 5;
                break;
            case R.id.button_kirim_ulasan:
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put(STATUS_KEY, BARANG_DITERIMA);
                transaksiRef.updateChildren(updateMap);
                finish();
                break;
        }
    }
}
