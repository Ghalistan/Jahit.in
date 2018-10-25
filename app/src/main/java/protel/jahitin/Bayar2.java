package protel.jahitin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class Bayar2 extends AppCompatActivity implements View.OnClickListener{
    Toolbar myToolbar;
    private Button btnMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_bayar2);
        super.onCreate(savedInstanceState);

        myToolbar = findViewById(R.id.Bayar2_toolbar);
        myToolbar.setTitle("Konfirmasi Pembayaran");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(myToolbar);

        btnMove = findViewById(R.id.Bayar2Button);
        btnMove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bayar2Button:
                break;
        }
    }
}
