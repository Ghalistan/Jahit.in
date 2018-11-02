package protel.jahitin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import protel.jahitin.Fragment.KeranjangFragment;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;

public class Bayar2 extends AppCompatActivity implements View.OnClickListener{
    private Toolbar myToolbar;
    private TextView addBukti, tanggalKonfTextView, totalHargaTextView;
    private ImageView logoBankImageView;
    private Button btnMove;

    private int totalHarga;
    private String tanggal, caraBayar, transaksiKey;

    private DatabaseReference transaksiDatabaseReference;
    private ValueEventListener transaksiValueEventListener;
    private FirebaseUser mUser;

    public static final String EXTRA_BAYAR_FRAGMENT = "bayar_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_bayar2);
        super.onCreate(savedInstanceState);

        Intent intentAsal = getIntent();
        if(intentAsal != null && intentAsal.hasExtra(Bayar.EXTRA_TRANSAKSI_KEY)){
            transaksiKey = intentAsal.getStringExtra(Bayar.EXTRA_TRANSAKSI_KEY);
        }

        myToolbar = findViewById(R.id.Bayar2_toolbar);
        myToolbar.setTitle("Konfirmasi Pembayaran");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(this);
        setSupportActionBar(myToolbar);

        tanggalKonfTextView = findViewById(R.id.konf_tanggal);
        totalHargaTextView = findViewById(R.id.tv_total_harga_bayar2);
        logoBankImageView = findViewById(R.id.bank_icon);

        btnMove = findViewById(R.id.Bayar2Button);
        btnMove.setOnClickListener(this);
        addBukti = findViewById(R.id.add_bukti);
        addBukti.setOnClickListener(this);

        //Log.d("Bayar2", "Key: " + transaksiKey);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        transaksiDatabaseReference = FirebaseDatabase.getInstance().getReference().child("transaksi").child(mUser.getUid()).child(transaksiKey);
        attachDatabaseListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.Bayar2Button:
                Intent intent = new Intent(Bayar2.this, Beranda.class);
                intent.putExtra(EXTRA_BAYAR_FRAGMENT, true);
                startActivity(intent);
                break;
            case R.id.add_bukti:
                break;
            case R.drawable.ic_arrow_back_black_24dp:
                finish();
        }
    }

    public String getBatasPembayaran(long currentTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM yyyy, hh:mm zzz", new Locale("id", "ID"));
        Calendar waktuPesan = Calendar.getInstance();
        waktuPesan.setTimeInMillis(currentTime);
        waktuPesan.add(Calendar.HOUR, 12);

        String batasPembayaran = formatter.format(waktuPesan.getTime());

        return batasPembayaran;
    }

    public void attachDatabaseListener(){
        if(transaksiValueEventListener == null){
            transaksiValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Transaksi t = dataSnapshot.getValue(Transaksi.class);
                    Log.d("Transaksi: ", t.getUserId());
                    caraBayar = t.getCaraBayar();
                    totalHarga = t.getTotalHarga();

                    long currentTimeMilis = t.getWaktuTransaksi();
                    String stringResult = null;
                    try {
                        stringResult = getBatasPembayaran(currentTimeMilis);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    totalHargaTextView.setText("Rp " + String.valueOf(totalHarga));
                    tanggalKonfTextView.setText(stringResult);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            transaksiDatabaseReference.addValueEventListener(transaksiValueEventListener);
        }
    }

    public void detachDatabaseListener(){
        if(transaksiValueEventListener != null){
            transaksiDatabaseReference.removeEventListener(transaksiValueEventListener);
            transaksiValueEventListener = null;
        }
    }
}
