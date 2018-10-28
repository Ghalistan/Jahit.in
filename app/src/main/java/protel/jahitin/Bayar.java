package protel.jahitin;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class Bayar extends AppCompatActivity implements View.OnClickListener{
    Button BayarButton;
    Toolbar myToolbar;
    RelativeLayout expandButton, title;
    ExpandableRelativeLayout cara_expand;
    boolean cek_expand = false;

    RecyclerView rvCara;
    RecyclerView.Adapter rvCaraAdapter;
    RecyclerView.LayoutManager rvCaraManager;

    private String[] data = {"mbleh1", "mbleh2", "mbleh3"};

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

        BayarButton = findViewById(R.id.BayarButton);
        BayarButton.setOnClickListener(this);
        expandButton = findViewById(R.id.expand_icon);
        expandButton.setOnClickListener(this);
        title = findViewById(R.id.cara_title);
        title.setOnClickListener(this);
        cara_expand = findViewById(R.id.expandable);
        cara_expand.collapse();
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void toggleCara() {
        if (cek_expand == false) {
            createRotateAnimator(expandButton, 0f, 180f).start();
            cek_expand = true;
            cara_expand.toggle();
        } else {
            createRotateAnimator(expandButton, 180f, 0f).start();
            cek_expand = false;
            cara_expand.toggle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BayarButton:
                startActivity(new Intent(Bayar.this, Bayar2.class));
                break;
            case R.id.expand_icon:
                toggleCara();
            case R.id.cara_title:
                toggleCara();
        }
    }
}
