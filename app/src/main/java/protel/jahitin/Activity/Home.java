package protel.jahitin.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import protel.jahitin.Utils.BottomNavigationBehavior;
import protel.jahitin.Fragment.BerandaFragment;
import protel.jahitin.Fragment.KeranjangFragment;
import protel.jahitin.R;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadFragment(new BerandaFragment());

        Toolbar toolbar = findViewById(R.id.beranda_toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mNavigationListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavigationListener =
        new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()){
                    case R.id.nav_beranda:
                        fragment = new BerandaFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.nav_keranjang:
                        fragment = new KeranjangFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.nav_transaksi:
                        return true;
                    case R.id.nav_akun:
                        return true;
                    default:
                        return false;
                }
            }
        };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fm_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
