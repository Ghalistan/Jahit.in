package protel.jahitin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class Bayar extends AppCompatActivity implements View.OnClickListener{
    Toolbar myToolbar;
    Button setBtn;
    ExpandableRelativeLayout exp;

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
                return;
            }
        });
        setSupportActionBar(myToolbar);

        setBtn = findViewById(R.id.BayarButton);
        setBtn.setOnClickListener(this);

        exp = findViewById(R.id.expandable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BayarButton:
                //startActivity(new Intent(Bayar.this, Bayar2.class));
                exp.toggle();
                break;
        }
    }
}
