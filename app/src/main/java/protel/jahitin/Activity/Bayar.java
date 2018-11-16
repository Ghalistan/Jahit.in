package protel.jahitin.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import protel.jahitin.Model.Keranjang;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;

public class Bayar extends AppCompatActivity implements View.OnClickListener{
    private TextView bMandiri, bBNI, kJNE, kTIKI, tvCara, tvKurir,
            tvAlamat, tvHargaBarang, tvHargaKurir, tvTotalHarga;
    private ExpandableRelativeLayout expandCara, expandKurir;
    private RelativeLayout caraIcon, kurirIcon, cara_title, kurir_title;
    private Toolbar myToolbar;
    private Button bayarButton;

    private boolean cekCara = false;
    private boolean cekKurir = false;

    private Transaksi transaksi;

    private List<Object> listBarang = new ArrayList<>();
    private List<Object> listJumlah = new ArrayList<>();
    private List<Pakaian> listPakaian = new ArrayList<>();
    private List<Keranjang> listKeranjang = new ArrayList<>();
    private List<String> listPakaianKey = new ArrayList<>();
    private List<String> listKeranjangKey = new ArrayList<>();
    private int totalBarang, hargaKurir, totalHarga;

    private FirebaseDatabase firebaseDatabase;
    private ChildEventListener keranjangEventListener;
    private ValueEventListener pakaianValueEventListener;
    private DatabaseReference keranjangDatabaseReference, pakaianDatabaseReference, transaksiDatabaseReference;
    private FirebaseUser mUser;

    public static final String EXTRA_TRANSAKSI_KEY = "key_transaksi";
    public static final String EXTRA_INTENT_BAYAR = "intent_bayar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_bayar);
        super.onCreate(savedInstanceState);

        myToolbar = findViewById(R.id.Bayar_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Pembayaran");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Bayar.this, Beranda.class);
                intent.putExtra(EXTRA_INTENT_BAYAR, true);
                finish();
                startActivity(intent);
            }
        });



        transaksi = new Transaksi();
        hargaKurir =  11000;
        totalHarga = 0;

        initUIComponent();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        keranjangDatabaseReference = firebaseDatabase.getReference().child("keranjang").child(mUser.getUid());
        pakaianDatabaseReference = firebaseDatabase.getReference().child("pakaian");
        transaksiDatabaseReference = firebaseDatabase.getReference().child("transaksi").child(mUser.getUid());


        attachDatabaseReadListener();
    }

    private void Rotator(View view) {
        switch (view.getId()) {
            case R.id.cara_title:
            case R.id.bMandiri:
            case R.id.bBNI:
                if (!cekCara) {
                    createRotateAnimator(caraIcon, 0f, 180f).start();
                    cekCara = true;
                } else {
                    createRotateAnimator(caraIcon, 180f, 0f).start();
                    cekCara = false;
                }
                break;
            case R.id.kurir_title:
            case R.id.kJNE:
            case R.id.kTIKI:
                if (!cekKurir) {
                    createRotateAnimator(kurirIcon, 0f, 180f).start();
                    cekKurir = true;
                } else {
                    createRotateAnimator(kurirIcon, 180f, 0f).start();
                    cekKurir = false;

                    String jenisKurir = tvKurir.getText().toString().trim();
                    switch (jenisKurir){
                        case "JNE":
                            hargaKurir = 11000;
                            break;
                        case "TIKI":
                            hargaKurir = 12000;
                            break;
                        default:
                            hargaKurir = 15000;
                            break;
                    }

                    transaksi.setHargaKurir(hargaKurir);
                    String stringHargaKurir = "Rp " + String.valueOf(hargaKurir);
                    tvHargaKurir.setText(stringHargaKurir);
                    totalHarga = hargaKurir + totalBarang;
                    String stringTotal = "Rp " + String.valueOf(totalHarga);
                    tvTotalHarga.setText(stringTotal);
                }
        }
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Cara Pembayaran
            case R.id.cara_title:
                expandCara.toggle();
                break;
            case R.id.bMandiri:
                tvCara.setText("Mandiri");
                expandCara.collapse();
                break;
            case R.id.bBNI:
                tvCara.setText("BNI");
                expandCara.collapse();
                break;
            //Kurir
            case R.id.kurir_title:
                expandKurir.toggle();
                break;
            case R.id.kJNE:
                tvKurir.setText("JNE");
                expandKurir.collapse();
                break;
            case R.id.kTIKI:
                tvKurir.setText("TIKI");
                expandKurir.collapse();
                break;
            case R.id.BayarButton:
                if(createTransaksi()) {
                    DatabaseReference transaksiRef = transaksiDatabaseReference.push();
                    String transaksiKey = transaksiRef.getKey();
                    Log.d("Bayar", "Key: " + transaksiKey);

                    Calendar waktuSekarang = Calendar.getInstance(new Locale("id", "ID"));
                    transaksi.setWaktuTransaksi(waktuSekarang.getTimeInMillis());
                    transaksiRef.setValue(transaksi);

                    Intent intent = new Intent(Bayar.this, Bayar2.class);
                    intent.putExtra(EXTRA_TRANSAKSI_KEY, transaksiKey);
                    startActivity(intent);
                    finish();
                    break;
                }else{
                    Toast toast = Toast.makeText
                            (this, "Isi data pembayaran terlebih dahulu", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }
            case R.drawable.ic_arrow_back_black_24dp:
                Log.d(Bayar.class.getSimpleName(), "Clicked");
                break;
        }

        Rotator(view);
    }

    public void initUIComponent(){
        bMandiri = findViewById(R.id.bMandiri);
        bMandiri.setOnClickListener(this);
        bBNI = findViewById(R.id.bBNI);
        bBNI.setOnClickListener(this);
        kJNE = findViewById(R.id.kJNE);
        kJNE.setOnClickListener(this);
        kTIKI = findViewById(R.id.kTIKI);
        kTIKI.setOnClickListener(this);
        expandCara = findViewById(R.id.cara_expandable);
        expandCara.collapse();
        expandKurir = findViewById(R.id.kurir_expandable);
        expandKurir.collapse();
        cara_title = findViewById(R.id.cara_title);
        cara_title.setOnClickListener(this);
        kurir_title = findViewById(R.id.kurir_title);
        kurir_title.setOnClickListener(this);
        bayarButton = findViewById(R.id.BayarButton);
        bayarButton.setOnClickListener(this);

        tvCara = findViewById(R.id.tvCara);
        tvKurir = findViewById(R.id.tvKurir);
        tvAlamat = findViewById(R.id.et_alamat_pengiriman);
        caraIcon = findViewById(R.id.cara_expand_icon);
        kurirIcon = findViewById(R.id.kurir_expand_icon);

        tvHargaBarang = findViewById(R.id.nominal_barang);
        tvHargaKurir = findViewById(R.id.nominal_kurir);
        tvTotalHarga = findViewById(R.id.nominal_total);
    }

    public boolean createTransaksi(){
        if(tvAlamat.getText() != null && !tvAlamat.getText().toString().isEmpty()){
            String alamat = tvAlamat.getText().toString();
            transaksi.setAlamat(alamat);
        }else return false;

        if(tvCara.getText() != null && !tvCara.getText().toString().equals("Cara Pembayaran")){
            String caraPembayaran = tvCara.getText().toString();
            transaksi.setCaraBayar(caraPembayaran);
        }else return false;

        if(tvKurir.getText() != null && !tvKurir.getText().toString().equals("Kurir Pengiriman")){
            String kurir = tvKurir.getText().toString();
            transaksi.setKurir(kurir);
        }

        transaksi.setHargaBarang(totalBarang);
        transaksi.setJumlah(listJumlah);
        transaksi.setUserId(mUser.getUid());
        transaksi.setStatus("Menunggu Pembayaran");
        transaksi.setBarang(listBarang);
        transaksi.setTotalHarga(totalHarga);
        transaksi.setRekTujuan("123 123 1234567");

        return true;
    }

    public void attachDatabaseReadListener(){
        if(keranjangEventListener == null){
            keranjangEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Keranjang k = dataSnapshot.getValue(Keranjang.class);
                    listKeranjang.add(k);
                    listJumlah.add(k.getJumlah());
                    listPakaianKey.add(k.getIdBarang());
                    listKeranjangKey.add(dataSnapshot.getKey());
                    Log.d("here", k.getIdBarang());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //
                }
            };

            keranjangDatabaseReference.addChildEventListener(keranjangEventListener);
        }


        if(pakaianValueEventListener == null){
            pakaianValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = 0;
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Pakaian p = data.getValue(Pakaian.class);
                        if(listPakaianKey.contains(data.getKey())) {
                            listBarang.add(data.getKey());
                            listPakaian.add(p);

                            totalBarang += listPakaian.get(i).getHarga() * listKeranjang.get(i).getJumlah();
                            i++;
                        }
                    }

                    String stringHargaBarang = "Rp " + String.valueOf(totalBarang);
                    tvHargaBarang.setText(stringHargaBarang);

                    totalHarga = totalBarang + hargaKurir;
                    String stringTotal = "Rp " + String.valueOf(totalHarga);
                    tvTotalHarga.setText(stringTotal);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            pakaianDatabaseReference.addValueEventListener(pakaianValueEventListener);
        }
    }

    public void detachDatabaseReadListener(){
        if(keranjangEventListener != null){
            keranjangDatabaseReference.removeEventListener(keranjangEventListener);
            keranjangEventListener = null;
        }

        if(pakaianValueEventListener != null){
            pakaianDatabaseReference.removeEventListener(pakaianValueEventListener);
            pakaianValueEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }
}
