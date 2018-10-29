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

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;

public class Bayar extends AppCompatActivity implements View.OnClickListener{
    Button BayarButton;
    Toolbar myToolbar;
    RelativeLayout expand_cara, expand_kurir, title_cara, title_kurir;
    ExpandableRelativeLayout cara_expand, kurir_expand;
    boolean cek_expand = false;
    BayarAdapter adapter;
    RecyclerView rvCara, rvKurir;

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
    }

    private void setCara() {
        ArrayList<String>caraPembayaran = new ArrayList<>();
        caraPembayaran.add("Transfer Mandiri");
        caraPembayaran.add("Transfer BNI");

        expand_cara = findViewById(R.id.cara_expand_icon);
        title_cara = findViewById(R.id.cara_title);
        title_cara.setOnClickListener(this);
        cara_expand = findViewById(R.id.cara_expandable);
        cara_expand.collapse();

        rvCara = findViewById(R.id.cara_data);
        rvCara.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BayarAdapter(this, caraPembayaran);
        rvCara.setAdapter(adapter);
    }

    private void setKurir() {
        ArrayList<String>kurirPengiriman = new ArrayList<>();
        kurirPengiriman.add("JNE");
        kurirPengiriman.add("TIKI");

        expand_kurir = findViewById(R.id.kurir_expand_icon);
        title_kurir = findViewById(R.id.kurir_title);
        title_kurir.setOnClickListener(this);
        kurir_expand = findViewById(R.id.kurir_expandable);
        kurir_expand.collapse();

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
        if (cek_expand == false) {
            createRotateAnimator(layout, 0f, 180f).start();
            cek_expand = true;
            dropdown.toggle();
        } else {
            createRotateAnimator(layout, 180f, 0f).start();
            cek_expand = false;
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
                toggleDropdown(expand_cara, cara_expand);
            case R.id.kurir_title:
                toggleDropdown(expand_kurir, kurir_expand);
        }
    }
}
