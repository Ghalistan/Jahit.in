package protel.jahitin.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import protel.jahitin.R;

public class DetailBarang extends AppCompatActivity {
    private Toolbar myToolbar;
    private TextView tvBahan, tvSize, tvHarga, tvCatatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_detail_barang);
        super.onCreate(savedInstanceState);

        myToolbar = findViewById(R.id.toolbar_detail_barang);
        myToolbar.setTitle("Informasi Barang");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvBahan = findViewById(R.id.tvBahan);
        tvSize = findViewById(R.id.tvSize);
        tvHarga = findViewById(R.id.tvHarga);
        tvCatatan = findViewById(R.id.tvCatatan);
    }
}
