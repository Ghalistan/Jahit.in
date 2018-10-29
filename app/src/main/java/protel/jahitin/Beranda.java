package protel.jahitin;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
// Gal Beranda nya yang di folder Activity aja ya
public class Beranda extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_beranda);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
    }
}
