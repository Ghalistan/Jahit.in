package protel.jahitin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import protel.jahitin.Fragment.AkunFragment;
import protel.jahitin.Fragment.PakaianJadiFragment;
import protel.jahitin.Fragment.TransaksiFragment;
import protel.jahitin.Utils.BottomNavigationBehavior;
import protel.jahitin.Fragment.BerandaFragment;
import protel.jahitin.Fragment.KeranjangFragment;
import protel.jahitin.R;

public class Beranda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mNavigationListener);

        Intent intentAsal = getIntent();
        if(intentAsal != null && intentAsal.hasExtra(Intent.EXTRA_TEXT)){
            Log.d("Beranda", String.valueOf(intentAsal.getIntExtra(Intent.EXTRA_TEXT, -1)));
            if(intentAsal.getIntExtra(Intent.EXTRA_TEXT, -1) == PakaianJadiFragment.EXTRA_PAKAIAN_JADI_FRAGMENT){
                loadFragment(new KeranjangFragment());

                MenuItem item = bottomNavigationView.getMenu().getItem(1);
                Log.d("Beranda", String.valueOf(item.getItemId()));
                item.setChecked(true);
            }
        }else{
            loadFragment(new BerandaFragment());
        }


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavigationListener =
        new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fm_container);
                switch(item.getItemId()){
                    case R.id.nav_beranda:
                        if(!(fragment instanceof BerandaFragment)){
                            fragment = new BerandaFragment();
                            loadFragment(fragment);
                        }
                        return true;
                    case R.id.nav_keranjang:
                        if(!(fragment instanceof KeranjangFragment)) {
                            fragment = new KeranjangFragment();
                            loadFragment(fragment);
                        }
                        return true;
                    case R.id.nav_transaksi:
                        if(!(fragment instanceof TransaksiFragment)) {
                            fragment = new TransaksiFragment();
                            loadFragment(fragment);
                        }
                        return true;
                    case R.id.nav_akun:
                        if(!(fragment instanceof AkunFragment)) {
                            fragment = new AkunFragment();
                            loadFragment(fragment);
                        }
                        return true;
                }
                return false;
            }
        };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fm_container, fragment);
        transaction.commit();
    }
}
