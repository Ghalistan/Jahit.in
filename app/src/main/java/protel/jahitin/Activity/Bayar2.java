package protel.jahitin.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import protel.jahitin.Model.Transaksi;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

public class Bayar2 extends AppCompatActivity implements View.OnClickListener{
    private Toolbar myToolbar;
    private TextView addBukti, tanggalKonfTextView, totalHargaTextView;
    private ImageView logoBankImageView, buktiPembayaranImageView;
    private Button btnMove;
    private RelativeLayout containerLabelUpload;
    private LinearLayout containerHasilUpload;
    private ProgressBar progressBar;
    private ProgressBarUtils pbUtils;

    private int totalHarga;
    private String tanggal, caraBayar, transaksiKey;

    private DatabaseReference transaksiDatabaseReference, keranjangDatabaseReference;
    private StorageReference buktiPembayaranStorageReference;
    private ValueEventListener transaksiValueEventListener;
    private FirebaseUser mUser;

    public static final int RC_PHOTO_PICKER = 1;

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

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        transaksiDatabaseReference = FirebaseDatabase.getInstance().getReference().child("transaksi").child(mUser.getUid()).child(transaksiKey);
        buktiPembayaranStorageReference = FirebaseStorage.getInstance().getReference().child("bukti_pembayaran");
        keranjangDatabaseReference = FirebaseDatabase.getInstance().getReference().child("keranjang").child(mUser.getUid());

        tanggalKonfTextView = findViewById(R.id.konf_tanggal);
        totalHargaTextView = findViewById(R.id.tv_total_harga_bayar2);
        logoBankImageView = findViewById(R.id.bank_icon);
        progressBar = findViewById(R.id.pb_bayar2);
        containerLabelUpload = findViewById(R.id.label_upload_bukti_pembayaran);
        containerHasilUpload = findViewById(R.id.label_hasil_upload);

        buktiPembayaranImageView = findViewById(R.id.iv_bukti_pembayaran);
        buktiPembayaranImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBuktiPembayaran();
            }
        });

        myToolbar = findViewById(R.id.Bayar2_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Konfirmasi Pembayaran");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                transaksiDatabaseReference.removeValue();
            }
        });

        btnMove = findViewById(R.id.Bayar2Button);
        btnMove.setOnClickListener(this);
        addBukti = findViewById(R.id.add_bukti);
        addBukti.setOnClickListener(this);

        pbUtils = new ProgressBarUtils();

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
                if(containerHasilUpload.getVisibility() == View.GONE){
                    Toast.makeText(getApplicationContext(), "Upload bukti pembayaran terlebih dahulu", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Intent intent = new Intent(Bayar2.this, Beranda.class);
                    intent.putExtra(EXTRA_BAYAR_FRAGMENT, true);
                    startActivity(intent);
                    finish();
                    keranjangDatabaseReference.removeValue();
                }

                break;
            case R.id.add_bukti:
                uploadBuktiPembayaran();
                break;
            case R.drawable.ic_arrow_back_black_24dp:
                transaksiDatabaseReference.removeValue();
                finish();
                break;
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
                    if(dataSnapshot.hasChild("userId")){
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

    public void uploadBuktiPembayaran(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            if(containerLabelUpload.getVisibility() == View.VISIBLE){
                containerLabelUpload.setVisibility(View.GONE);
            }

            if(buktiPembayaranImageView.getVisibility() == View.VISIBLE){
                buktiPembayaranImageView.setVisibility(View.GONE);
            }

            containerHasilUpload.setVisibility(View.VISIBLE);
            pbUtils.showLoadingIndicator(progressBar);

            Uri gambarUri = data.getData();
            final StorageReference gambarRef = buktiPembayaranStorageReference
                    .child(gambarUri.getLastPathSegment());

            gambarRef.putFile(gambarUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return gambarRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        buktiPembayaranImageView.setVisibility(View.VISIBLE);

                        Uri downloadUri = task.getResult();
                        Log.d(Bayar2.class.getSimpleName(), downloadUri.toString());
                        Glide.with(Bayar2.this).load(downloadUri.toString())
                                .into(buktiPembayaranImageView);
                        transaksiDatabaseReference.child("buktiPembayaranUrl").setValue(downloadUri.toString());
                        pbUtils.hideLoadingIndicator(progressBar);
                    } else {

                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transaksiDatabaseReference.removeValue();
    }
}
