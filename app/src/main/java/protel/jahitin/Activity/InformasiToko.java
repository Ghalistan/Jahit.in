package protel.jahitin.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import protel.jahitin.R;

public class InformasiToko extends AppCompatActivity implements View.OnClickListener {
    TextView alamatToko, rating, tvLainnya;
    ImageView fotoToko;
    ImageView star0, star1, star2, star3, star4;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_informasi_toko);
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.infotoko_toolbar);
        toolbar.setTitle("Informasi Toko");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(this);

        fotoToko = findViewById(R.id.foto_toko);
        alamatToko = findViewById(R.id.alamat_toko);
        rating = findViewById(R.id.rating_text);
        tvLainnya = findViewById(R.id.lainnya);
        tvLainnya.setOnClickListener(this);

        star0 = findViewById(R.id.star_0);
        star1 = findViewById(R.id.star_1);
        star2 = findViewById(R.id.star_2);
        star3 = findViewById(R.id.star_3);
        star4 = findViewById(R.id.star_4);

        generateStar();
    }

    public void generateStar() {
        double x = Double.parseDouble(rating.getText().toString());
        if ( x < 1) {
            star0.setImageResource(R.drawable.star3);
            star1.setImageResource(R.drawable.star3);
            star2.setImageResource(R.drawable.star3);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 2) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star3);
            star2.setImageResource(R.drawable.star3);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 3) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star3);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 4) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star2);
            star3.setImageResource(R.drawable.star3);
            star4.setImageResource(R.drawable.star3);
        } else if (x < 5) {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star2);
            star3.setImageResource(R.drawable.star2);
            star4.setImageResource(R.drawable.star3);
        } else {
            star0.setImageResource(R.drawable.star2);
            star1.setImageResource(R.drawable.star2);
            star2.setImageResource(R.drawable.star2);
            star3.setImageResource(R.drawable.star2);
            star4.setImageResource(R.drawable.star2);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lainnya:
                generateStar();
                break;
            case R.drawable.ic_arrow_back_black_24dp:
                finish();
                break;
        }
    }
}
