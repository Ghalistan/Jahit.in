package protel.jahitin.Fragment;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import protel.jahitin.Activity.Beranda;
import protel.jahitin.Model.Keranjang;
import protel.jahitin.Model.Pakaian;
import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianCustomFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout Jenis_tombol, Gender_tombol, Kain_tombol, Warna_tombol, Size_tombol;
    private ExpandableRelativeLayout Jenis_data, Gender_data, Kain_data, Warna_data, Size_data;
    private TextView tvJenis, tvGender, tvKain, tvWarna, tvSize, est_biaya, addFoto;
    private EditText keterangan;
    private Button btnPesan;
    private ProgressBar progressBar;
    private ProgressBarUtils pbUtils;
    private ImageView containerHasilUpload;
    private RelativeLayout containerLabelUpload;
    private ImageView gambarCustom;

    int jPakaian, jKain, Warna, Size, Total = 0;
    private Toast mToast;

    private boolean cekJenis = false;
    private boolean cekGender = false;
    private boolean cekKain = false;
    private boolean cekWarna = false;
    private boolean cekSize = false;
    private String jenis, gender, kain, warna, ukuran, keyToko, textKeterangan;
    public Uri gambarCustomUri;

    private DatabaseReference pakaianDatabaseReference, keranjangDatabaseReference;
    private StorageReference pakaianCustomStorageReference;
    private FirebaseUser mUser;

    private Keranjang keranjang;
    private Pakaian pakaianCustom;

    public static final int GENDER_PRIA = 0;
    public static final int GENDER_WANITA = 1;
    public static final String DEFAULT_PAKAIAN = "Jenis Pakaian";
    public static final String DEFAULT_KELAMIN = "Jenis Kelamin";
    public static final String DEFAULT_KAIN = "Jenis Kain";
    public static final String DEFAULT_WARNA = "Warna";
    public static final String DEFAULT_UKURAN = "Ukuran Pakaian";
    public static final int RC_PHOTO_PICKER = 1;
    public static final String IMAGE_CUSTOM_URL = "https://firebasestorage.googleapis.com/v0/b/jahitin-61346.appspot.com/o/gambar_pakaian%2Fcustom.png?alt=media&token=d4275a79-57a0-4716-accd-588f4c4c315d";
    public static int EXTRA_PAKAIAN_JADI_FRAGMENT = 1;

    public PakaianCustomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pakaian_custom, container, false);
        setJenis(view);
        setGender(view);
        setKain(view);
        setWarna(view);
        setSize(view);
        containerLabelUpload = view.findViewById(R.id.container_input_custom);
        containerHasilUpload = view.findViewById(R.id.iv_custom_desain);
        progressBar = view.findViewById(R.id.pb_pakaian_custom);
        pbUtils = new ProgressBarUtils();

        gambarCustom = view.findViewById(R.id.iv_custom_desain);
        gambarCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCustomDesain();
            }
        });

        keranjang = new Keranjang();
        pakaianCustom = new Pakaian();

        Intent intentAsal = getActivity().getIntent();
        keyToko = "";
        if(intentAsal != null){
            if(intentAsal.hasExtra(BerandaFragment.EXTRA_KEY_TOKO)){
                Log.d(PakaianJadiFragment.class.getSimpleName(), intentAsal.getStringExtra(BerandaFragment.EXTRA_KEY_TOKO));
                keyToko = intentAsal.getStringExtra(BerandaFragment.EXTRA_KEY_TOKO);
            }
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        keranjangDatabaseReference = FirebaseDatabase.getInstance().getReference().child("keranjang").child(mUser.getUid());
        pakaianDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pakaian");
        pakaianCustomStorageReference = FirebaseStorage.getInstance().getReference().child("gambar_custom");

        btnPesan = view.findViewById(R.id.PesanButton);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    generatePakaianCustom();
                    DatabaseReference ref = pakaianDatabaseReference.push();
                    String pakaianKey = ref.getKey();

                    Map<String, Object> mapPakaian = new HashMap<>();
                    mapPakaian.put(pakaianKey, pakaianCustom);
                    pakaianDatabaseReference.updateChildren(mapPakaian);

                    keranjang = new Keranjang(pakaianKey, keyToko, 1);
                    Map<String, Object> mapKeranjang = new HashMap<>();
                    mapKeranjang.put(pakaianKey, keranjang);
                    keranjangDatabaseReference.updateChildren(mapKeranjang);

                    Intent intent = new Intent(getActivity(), Beranda.class);
                    intent.putExtra(Intent.EXTRA_TEXT, EXTRA_PAKAIAN_JADI_FRAGMENT);
                    getActivity().startActivity(intent);
                }
            }
        });

        addFoto = view.findViewById(R.id.add_file_foto);
        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCustomDesain();
            }
        });

        est_biaya = view.findViewById(R.id.estimasi_biaya);
        keterangan = view.findViewById(R.id.keterangan);

        return view;
    }

    private void estimasi() {
        Total = jPakaian + jKain + Warna + Size;

        est_biaya.setText("Rp " + Integer.toString(Total));
    }

    private void rotator(View view) {
        switch (view.getId()) {
            case R.id.jpakaian_layout:
            case R.id.Kaos_pcustom:
            case R.id.Kemeja_pcustom:
            case R.id.Polo_pcustom:
                if(!cekJenis) {
                    createRotateAnimator(Jenis_tombol, 0f, 180f).start();
                    cekJenis = true;
                } else {
                    createRotateAnimator(Jenis_tombol, 180f, 0f).start();
                    cekJenis = false;
                }
                break;
            case R.id.gender_layout:
            case R.id.male_gender:
            case R.id.female_gender:
                if(!cekGender) {
                    createRotateAnimator(Gender_tombol, 0f, 180f).start();
                    cekGender = true;
                } else {
                    createRotateAnimator(Gender_tombol, 180f, 0f).start();
                    cekGender = false;
                }
                break;
            case R.id.jkain_layout:
            case R.id.katun_kain:
            case R.id.spandex_kain:
                if(!cekKain) {
                    createRotateAnimator(Kain_tombol, 0f, 180f).start();
                    cekKain = true;
                } else {
                    createRotateAnimator(Kain_tombol, 180f, 0f).start();
                    cekKain = false;
                }
                break;
            case R.id.warna_layout:
            case R.id.merah_warna:
            case R.id.biru_warna:
            case R.id.hijau_warna:
                if(!cekWarna) {
                    createRotateAnimator(Warna_tombol, 0f, 180f).start();
                    cekWarna = true;
                } else {
                    createRotateAnimator(Warna_tombol, 180f, 0f).start();
                    cekWarna = false;
                }
                break;
            case R.id.size_layout:
            case R.id.small_size:
            case R.id.medium_size:
            case R.id.large_size:
            case R.id.xlarge_size:
                if(!cekSize) {
                    createRotateAnimator(Size_tombol, 0f, 180f).start();
                    cekSize = true;
                } else {
                    createRotateAnimator(Size_tombol, 180f, 0f).start();
                    cekSize = false;
                }
                break;
        }
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void setSize(View view) {
        RelativeLayout Size_title = view.findViewById(R.id.size_layout);
        Size_title.setOnClickListener(this);
        RelativeLayout Small = view.findViewById(R.id.small_size);
        Small.setOnClickListener(this);
        RelativeLayout Medium = view.findViewById(R.id.medium_size);
        Medium.setOnClickListener(this);
        RelativeLayout Large = view.findViewById(R.id.large_size);
        Large.setOnClickListener(this);
        RelativeLayout XLarge = view.findViewById(R.id.xlarge_size);
        XLarge.setOnClickListener(this);

        Size_tombol = view.findViewById(R.id.size_expand_icon);
        tvSize = view.findViewById(R.id.tvSize);
        Size_data = view.findViewById(R.id.size_expandable);
        Size_data.collapse();
    }

    private void setWarna(View view) {
        RelativeLayout Warna_title = view.findViewById(R.id.warna_layout);
        Warna_title.setOnClickListener(this);
        RelativeLayout Merah = view.findViewById(R.id.merah_warna);
        Merah.setOnClickListener(this);
        RelativeLayout Biru = view.findViewById(R.id.biru_warna);
        Biru.setOnClickListener(this);
        RelativeLayout Hijau = view.findViewById(R.id.hijau_warna);
        Hijau.setOnClickListener(this);

        Warna_tombol = view.findViewById(R.id.warna_expand_icon);
        tvWarna = view.findViewById(R.id.tvWarna);
        Warna_data = view.findViewById(R.id.warna_expandable);
        Warna_data.collapse();
    }

    private void setKain(View view) {
        RelativeLayout Kain_title = view.findViewById(R.id.jkain_layout);
        Kain_title.setOnClickListener(this);
        RelativeLayout Katun = view.findViewById(R.id.katun_kain);
        Katun.setOnClickListener(this);
        RelativeLayout Spandex = view.findViewById(R.id.spandex_kain);
        Spandex.setOnClickListener(this);

        Kain_tombol = view.findViewById(R.id.kain_expand_icon);
        tvKain = view.findViewById(R.id.tvKain);
        Kain_data = view.findViewById(R.id.kain_expandable);
        Kain_data.collapse();
    }

    private void setGender(View view) {
        RelativeLayout Gender_title = view.findViewById(R.id.gender_layout);
        Gender_title.setOnClickListener(this);
        RelativeLayout Male = view.findViewById(R.id.male_gender);
        Male.setOnClickListener(this);
        RelativeLayout Female = view.findViewById(R.id.female_gender);
        Female.setOnClickListener(this);

        Gender_tombol = view.findViewById(R.id.gender_expand_icon);
        tvGender = view.findViewById(R.id.tvGender);
        Gender_data = view.findViewById(R.id.gender_expandable);
        Gender_data.collapse();
    }

    private void setJenis(View view) {
        RelativeLayout Jenis_title =  view.findViewById(R.id.jpakaian_layout);
        Jenis_title.setOnClickListener(this);
        RelativeLayout Kaos = view.findViewById(R.id.Kaos_pcustom);
        Kaos.setOnClickListener(this);
        RelativeLayout Kemeja = view.findViewById(R.id.Kemeja_pcustom);
        Kemeja.setOnClickListener(this);
        RelativeLayout Polo = view.findViewById(R.id.Polo_pcustom);
        Polo.setOnClickListener(this);

        Jenis_tombol = view.findViewById(R.id.jenis_expand_icon);
        tvJenis = view.findViewById(R.id.tvJenis);
        Jenis_data = view.findViewById(R.id.jenis_expandable);
        Jenis_data.collapse();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Jenis Pakaian
            case R.id.jpakaian_layout:
                Jenis_data.toggle();
                break;
            case R.id.Kaos_pcustom:
                tvJenis.setText("Kaos");
                jPakaian = 20000;
                Jenis_data.collapse();
                break;
            case R.id.Kemeja_pcustom:
                tvJenis.setText("Kemeja");
                jPakaian = 20000;
                Jenis_data.collapse();
                break;
            case R.id.Polo_pcustom:
                tvJenis.setText("Polo");
                jPakaian = 20000;
                Jenis_data.collapse();
                break;
            //Gender
            case R.id.gender_layout:
                Gender_data.toggle();
                break;
            case R.id.male_gender:
                tvGender.setText("Laki-Laki");
                Gender_data.collapse();
                break;
            case R.id.female_gender:
                tvGender.setText("Perempuan");
                Gender_data.collapse();
                break;
            //Kain
            case R.id.jkain_layout:
                Kain_data.toggle();
                break;
            case R.id.katun_kain:
                tvKain.setText("Katun");
                jKain = 20000;
                Kain_data.collapse();
                break;
            case R.id.spandex_kain:
                tvKain.setText("Spandex");
                jKain = 20000;
                Kain_data.collapse();
                break;
            //Warna
            case R.id.warna_layout:
                Warna_data.toggle();
                break;
            case R.id.merah_warna:
                tvWarna.setText("Merah");
                Warna = 5000;
                Warna_data.collapse();
                break;
            case R.id.biru_warna:
                tvWarna.setText("Biru");
                Warna = 5000;
                Warna_data.collapse();
                break;
            case R.id.hijau_warna:
                tvWarna.setText("Hijau");
                Warna = 5000;
                Warna_data.collapse();
                break;
            //Size
            case R.id.size_layout:
                Size_data.toggle();
                break;
            case R.id.small_size:
                tvSize.setText("S");
                Size = 5000;
                Size_data.collapse();
                break;
            case R.id.medium_size:
                tvSize.setText("M");
                Size = 10000;
                Size_data.collapse();
                break;
            case R.id.large_size:
                tvSize.setText("L");
                Size = 15000;
                Size_data.collapse();
                break;
            case R.id.xlarge_size:
                tvSize.setText("XL");
                Size = 20000;
                Size_data.collapse();
                break;
        }
        hideKeyboard(keterangan);
        keterangan.clearFocus();
        rotator(view);
        estimasi();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            getImageUri(data);
        }
    }

    public boolean validate(){
        jenis = tvJenis.getText().toString();
        gender = tvGender.getText().toString();
        kain = tvKain.getText().toString();
        warna = tvWarna.getText().toString();
        ukuran = tvSize.getText().toString();

        if(jenis.equals(DEFAULT_PAKAIAN) || gender.equals(DEFAULT_KELAMIN) || kain.equals(DEFAULT_KAIN) ||
                warna.equals(DEFAULT_WARNA) || ukuran.equals(DEFAULT_UKURAN))
        {
            if(mToast != null) mToast = null;

            mToast = Toast.makeText(getActivity(), "Silahkan isi data terlebih dahulu", Toast.LENGTH_SHORT);
            mToast.show();
            return false;
        }

        return true;
    }

    public void generatePakaianCustom(){
        pakaianCustom.setBahan(kain);
        pakaianCustom.setGender(gender);
        pakaianCustom.setHarga(Total);
        pakaianCustom.setJenis(jenis);
        pakaianCustom.setNama("Pakaian Custom");
        pakaianCustom.setUkuranTersedia(new ArrayList<Object>(){{add(ukuran);}});
        pakaianCustom.setWarnaTersedia(new ArrayList<Object>(){{add(warna);}});
        pakaianCustom.setToko(keyToko);
        pakaianCustom.setImageUrl(IMAGE_CUSTOM_URL);

        textKeterangan = keterangan.getText().toString();
        if(!textKeterangan.isEmpty()){
            pakaianCustom.setKeterangan(textKeterangan);
        }

        if(gambarCustomUri != null){
            pakaianCustom.setDesainCustomUrl(gambarCustomUri.toString());
        }
    }

    public void uploadCustomDesain(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                RC_PHOTO_PICKER);
    }

    public void getImageUri(Intent data){
        if(containerLabelUpload.getVisibility() == View.VISIBLE){
            containerLabelUpload.setVisibility(View.GONE);
        }

        if(gambarCustom.getVisibility() == View.VISIBLE){
            gambarCustom.setVisibility(View.GONE);
        }

        containerHasilUpload.setVisibility(View.VISIBLE);
        pbUtils.showLoadingIndicator(progressBar);

        if(gambarCustomUri != null){
            pakaianCustomStorageReference.child(gambarCustomUri.getLastPathSegment())
                    .delete();
            gambarCustomUri = null;
        }

        Uri gambarUri = data.getData();
        final StorageReference gambarRef = pakaianCustomStorageReference
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
                    gambarCustom.setVisibility(View.VISIBLE);
                    gambarCustomUri = task.getResult();

                    Glide.with(getActivity()).load(gambarCustomUri.toString())
                            .into(gambarCustom);
                    pbUtils.hideLoadingIndicator(progressBar);
                } else {

                }
            }
        });
    }
}
