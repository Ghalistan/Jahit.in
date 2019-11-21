package protel.jahitin.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import protel.jahitin.Adapter.PembelianPagerAdapater;
import protel.jahitin.Fragment.BerandaFragment;
import protel.jahitin.Fragment.PakaianCustomFragment;
import protel.jahitin.Fragment.PakaianJadiFragment;
import protel.jahitin.R;


public class Pembelian extends AppCompatActivity
    implements View.OnClickListener {

    private String keyToko;
    public static final String EXTRA_INFORMASI_TOKO = "informasi_toko";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian);

        Toolbar topToolbar = findViewById(R.id.pembelian_toolbar);
        setSupportActionBar(topToolbar);

        Intent intentAsal = getIntent();
        if(intentAsal != null){
            if(intentAsal.hasExtra(BerandaFragment.EXTRA_NAMA_TOKO)){
                Log.d(Pembelian.class.getSimpleName(), intentAsal.getStringExtra(BerandaFragment.EXTRA_NAMA_TOKO));
                String namaToko = intentAsal.getStringExtra(BerandaFragment.EXTRA_NAMA_TOKO);
                setTitle(namaToko);
            }

            keyToko = "";
            if(intentAsal.hasExtra(BerandaFragment.EXTRA_KEY_TOKO)){
                Log.d(PakaianJadiFragment.class.getSimpleName(), intentAsal.getStringExtra(BerandaFragment.EXTRA_KEY_TOKO));
                keyToko = intentAsal.getStringExtra(BerandaFragment.EXTRA_KEY_TOKO);
            }
        }


        ViewPager viewPager = findViewById(R.id.vp_pembelian);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {

    }

    public void setupViewPager(ViewPager viewPager){
        PembelianPagerAdapater fragmentPagerAdapter = new PembelianPagerAdapater(getSupportFragmentManager(), this);
//        fragmentPagerAdapter.addFrag(new PakaianCustomFragment());
        fragmentPagerAdapter.addFrag(new PakaianJadiFragment());
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pembelian, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_informasi_toko:
                Intent intent = new Intent(this, InformasiToko.class);
                intent.putExtra(EXTRA_INFORMASI_TOKO, keyToko);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
