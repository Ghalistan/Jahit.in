package protel.jahitin.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import protel.jahitin.R;

public class Bayar extends AppCompatActivity implements View.OnClickListener{
    TextView bMandiri, bBNI, kJNE, kTIKI, tvCara, tvKurir;
    ExpandableRelativeLayout expandCara, expandKurir;
    RelativeLayout caraIcon, kurirIcon, cara_title, kurir_title;
    Toolbar myToolbar;
    Button bayarButton;

    boolean cekCara = false;
    boolean cekKurir = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_bayar);
        super.onCreate(savedInstanceState);

        myToolbar = findViewById(R.id.Bayar_toolbar);
        myToolbar.setTitle("Pembayaran");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(this);
        setSupportActionBar(myToolbar);

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
        caraIcon = findViewById(R.id.cara_expand_icon);
        kurirIcon = findViewById(R.id.kurir_expand_icon);
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
                Intent intent = new Intent(Bayar.this, Bayar2.class);
                startActivity(intent);
                break;
            case R.drawable.ic_arrow_back_black_24dp:
                finish();
                break;
        }
        Rotator(view);
    }
}
