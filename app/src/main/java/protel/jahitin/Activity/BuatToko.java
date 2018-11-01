package protel.jahitin.Activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import protel.jahitin.R;

public class BuatToko extends AppCompatActivity implements View.OnClickListener {
    TextView hargaKaos, hargaKemeja, hargaKatun, hargaSpandex, hargaPutih, hargaMerah, hargaBiru, hargaS, hargaM, hargaL;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_buat_toko);
        super.onCreate(savedInstanceState);

        myToolbar = findViewById(R.id.buatToko_Toolbar);
        myToolbar.setTitle("Buat Toko");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(this);
        setSupportActionBar(myToolbar);

        setHarga();
    }

    private void setHarga() {
        hargaKaos = findViewById(R.id.harga_kaos);
        hargaKemeja = findViewById(R.id.harga_kemeja);
        hargaKatun = findViewById(R.id.harga_katun);
        hargaSpandex = findViewById(R.id.harga_spandex);
        hargaPutih = findViewById(R.id.harga_putih);
        hargaMerah = findViewById(R.id.harga_merah);
        hargaBiru = findViewById(R.id.harga_biru);
        hargaS = findViewById(R.id.harga_small);
        hargaM = findViewById(R.id.harga_medium);
        hargaL = findViewById(R.id.harga_large);

        hargaKaos.setOnClickListener(this);
        hargaKemeja.setOnClickListener(this);
        hargaKatun.setOnClickListener(this);
        hargaSpandex.setOnClickListener(this);
        hargaPutih.setOnClickListener(this);
        hargaMerah.setOnClickListener(this);
        hargaBiru.setOnClickListener(this);
        hargaS.setOnClickListener(this);
        hargaM.setOnClickListener(this);
        hargaL.setOnClickListener(this);
    }

    private String setTitleDialog(View view) {
        switch (view.getId()) {
            case R.id.harga_kaos:
                return "Harga Kaos";
            case R.id.harga_kemeja:
                return "Harga Kemeja";
            case R.id.harga_katun:
                return "Harga Katun";
            case R.id.harga_spandex:
                return "Harga Spandex";
            case R.id.harga_putih:
                return "Harga Warna Putih";
            case R.id.harga_merah:
                return "Harga Warna Merah";
            case R.id.harga_biru:
                return "Harga Warna Bitu";
            case R.id.harga_small:
                return "Harga ukuran S";
            case R.id.harga_medium:
                return "Harga ukuran M";
            case R.id.harga_large:
                return "Harga Ukuran L";
        }
        return "";
    }

    public void InputBox(final View view) {
        String title = setTitleDialog(view);
        final TextView textView = findViewById(view.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText("Rp " + input.getText());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onClick(View view) {
        InputBox(view);
    }
}
