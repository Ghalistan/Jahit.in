package protel.jahitin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;

import protel.jahitin.Fragment.BerandaFragment;
import protel.jahitin.Fragment.PakaianJadiFragment;
import protel.jahitin.Model.Toko;
import protel.jahitin.R;

public class InformasiToko extends AppCompatActivity implements View.OnClickListener {
    TextView alamatToko, rating, tvLainnya, noTelp, hubToko;
    ImageView fotoToko;
    ImageView star0, star1, star2, star3, star4;
    Toolbar toolbar;
    private Intent intentAsal;
    private String keyToko;
    private Toko toko;

    private DatabaseReference tokoRef;
    private ValueEventListener tokoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_informasi_toko);
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.infotoko_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fotoToko = findViewById(R.id.foto_toko);
        alamatToko = findViewById(R.id.alamat_toko);
        rating = findViewById(R.id.rating_text);
        tvLainnya = findViewById(R.id.lainnya);
        tvLainnya.setOnClickListener(this);
        noTelp = findViewById(R.id.tv_noTelp);

        hubToko = findViewById(R.id.tv_hub_toko);
        hubToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomorTelp = noTelp.getText().toString();

                String url = "https://api.whatsapp.com/send?phone=" + nomorTelp;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        star0 = findViewById(R.id.star_0);
        star1 = findViewById(R.id.star_1);
        star2 = findViewById(R.id.star_2);
        star3 = findViewById(R.id.star_3);
        star4 = findViewById(R.id.star_4);

        intentAsal = getIntent();
        keyToko = "";
        if(intentAsal != null){
            if(intentAsal.hasExtra(Pembelian.EXTRA_INFORMASI_TOKO)){
                keyToko = intentAsal.getStringExtra(Pembelian.EXTRA_INFORMASI_TOKO);
                Log.d(InformasiToko.class.getSimpleName(), keyToko);
            }
        }

        tokoRef = FirebaseDatabase.getInstance().getReference().child("toko").child(keyToko);
        attachDatabaseReadListener();
    }

    public void generateStar() {
        double x = Double.parseDouble(rating.getText().toString());
        if ( x < 1) {
            star0.setImageResource(R.drawable.star3);
            star1.setImageResource(R.drawable.star3);
            star2.setImageResource(R.drawable.star3);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 2) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star3);
            star2.setImageResource(R.drawable.star3);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 3) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star3);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 4) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star2);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 5) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star2);
            star3.setImageResource(R.drawable.star2);
            star4.setImageResource(R.drawable.star3);
        } else {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star2);
            star3.setImageResource(R.drawable.star2);
            star4.setImageResource(R.drawable.star2);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lainnya:
                generateStar();
                break;
            case R.drawable.ic_arrow_back_black_24dp:
                finish();
                break;
        }
    }

    public void attachDatabaseReadListener(){
        Log.d("KEYTOKO", keyToko);
        if(tokoValue == null){
            tokoValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        toko = dataSnapshot.getValue(Toko.class);

                        Glide.with(InformasiToko.this).load(toko.getImageUrl())
                                .into(fotoToko);
                        alamatToko.setText(toko.getAlamat());
                        rating.setText(String.valueOf(toko.getRating()));
                        generateStar();
                        noTelp.setText(toko.getNoTelp());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            tokoRef.addListenerForSingleValueEvent(tokoValue);
        }
    }

    public void detachDatabaseListener(){
        if(tokoValue != null){
            tokoRef.removeEventListener(tokoValue);
            tokoValue = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseListener();
    }
}
