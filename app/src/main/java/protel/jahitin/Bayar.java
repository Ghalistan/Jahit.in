package protel.jahitin;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;

public class Bayar extends AppCompatActivity implements View.OnClickListener{
    Button BayarButton;
    Toolbar myToolbar;
    RelativeLayout icon_cara, icon_kurir, title_cara, title_kurir;
    ExpandableRelativeLayout expand_cara, expand_kurir;
    boolean cek_expand_cara = false;
    boolean cek_expand_kurir = false;
    BayarAdapter adapter;
    RecyclerView rvCara, rvKurir;

    TextView cara, kurir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_bayar);
        super.onCreate(savedInstanceState);

        myToolbar = findViewById(R.id.Bayar_toolbar);
        myToolbar.setTitle("Pembayaran");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(myToolbar);

        setCara();
        setKurir();

        BayarButton = findViewById(R.id.BayarButton);
        BayarButton.setOnClickListener(this);

        cara = findViewById(R.id.tvCara);
        kurir = findViewById(R.id.tvKurir);
    }

    private void setCara() {
        ArrayList<String>caraPembayaran = new ArrayList<>();
        caraPembayaran.add("Transfer Mandiri");
        caraPembayaran.add("Transfer BNI");

        icon_cara = findViewById(R.id.cara_expand_icon);
        title_cara = findViewById(R.id.cara_title);
        title_cara.setOnClickListener(this);
        expand_cara = findViewById(R.id.cara_expandable);
        expand_cara.collapse();

        rvCara = findViewById(R.id.cara_data);
        rvCara.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BayarAdapter(this, caraPembayaran);
        rvCara.setAdapter(adapter);
    }

    private void setKurir() {
        ArrayList<String>kurirPengiriman = new ArrayList<>();
        kurirPengiriman.add("JNE");
        kurirPengiriman.add("TIKI");

        icon_kurir = findViewById(R.id.kurir_expand_icon);
        title_kurir = findViewById(R.id.kurir_title);
        title_kurir.setOnClickListener(this);
        expand_kurir = findViewById(R.id.kurir_expandable);
        expand_kurir.collapse();

        rvKurir = findViewById(R.id.kurir_data);
        rvKurir.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BayarAdapter(this, kurirPengiriman);
        rvKurir.setAdapter(adapter);
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void toggleDropdown(RelativeLayout layout, ExpandableRelativeLayout dropdown) {
        if (!cek_expand_cara) {
            createRotateAnimator(layout, 0f, 180f).start();
            cek_expand_cara = true;
            cara.setText("true");
            dropdown.toggle();
        } else {
            createRotateAnimator(layout, 180f, 0f).start();
            cek_expand_cara = false;
            cara.setText("false");
            dropdown.toggle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BayarButton:
                startActivity(new Intent(Bayar.this, Bayar2.class));
                break;
            case R.id.cara_title:
                if (!cek_expand_cara) {
                    createRotateAnimator(icon_cara, 0f, 180f).start();
                    cek_expand_cara = true;
                    cara.setText("true");
                    expand_cara.toggle();
                } else {
                    createRotateAnimator(icon_cara, 180f, 0f).start();
                    cek_expand_cara = false;
                    cara.setText("false");
                    expand_cara.toggle();
                }
                // Jangan lupa tiap case ada break
                break;
            case R.id.kurir_title:
                if (!cek_expand_kurir) {
                    createRotateAnimator(icon_kurir, 0f, 180f).start();
                    // nih ganti jadi cek_expand_kurir
                    cek_expand_kurir = true;
                    kurir.setText("true");
                    expand_kurir.toggle();
                } else {
                    createRotateAnimator(icon_kurir, 180f, 0f).start();
                    // nih ganti jadi cek_expand_kurir
                    cek_expand_kurir = false;
                    kurir.setText("false");
                    expand_kurir.toggle();
                }
                // Jangan lupa tiap case ada break
                break;
        }
    }
}
