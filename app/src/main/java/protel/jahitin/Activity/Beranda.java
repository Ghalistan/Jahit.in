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
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import protel.jahitin.Fragment.AkunFragment;
import protel.jahitin.Fragment.PakaianJadiFragment;
import protel.jahitin.Fragment.TransaksiFragment;
import protel.jahitin.Utils.BottomNavigationBehavior;
import protel.jahitin.Fragment.BerandaFragment;
import protel.jahitin.Fragment.KeranjangFragment;
import protel.jahitin.R;

public class Beranda extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Intent intentAsal;
    private BottomNavigationView bottomNavigationView;
    private Intent loginIntent;

    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mNavigationListener);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){

                }else{
                    loginIntent = new Intent(Beranda.this, Login.class);
                    startActivityForResult(loginIntent, RC_SIGN_IN);

                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        intentAsal = getIntent();
        bukaFragmentDariIntent();

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                loadFragment(new BerandaFragment());
            }else if(resultCode == RESULT_CANCELED){
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
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

    private void bukaFragmentDariIntent(){
        Log.d("Here", intentAsal.toString());
        Menu bottomMenu = bottomNavigationView.getMenu();
        MenuItem item = bottomMenu.getItem(0);
        if(intentAsal != null){
            if(intentAsal.hasExtra(Intent.EXTRA_TEXT)){
                if(intentAsal.getIntExtra(Intent.EXTRA_TEXT, -1) ==
                                PakaianJadiFragment.EXTRA_PAKAIAN_JADI_FRAGMENT)
                {
                    loadFragment(new KeranjangFragment());

                    item = bottomMenu.getItem(1);
                    item.setChecked(true);
                    intentAsal = null;
                }
            }
            else if(intentAsal.hasExtra(Bayar2.EXTRA_BAYAR_FRAGMENT))
            {
                loadFragment(new TransaksiFragment());

                item = bottomMenu.getItem(2);
                item.setChecked(true);
                intentAsal = null;
            }
            else if(intentAsal.hasExtra(Bayar.EXTRA_INTENT_BAYAR))
            {
                loadFragment(new KeranjangFragment());

                item = bottomMenu.getItem(1);
                item.setChecked(true);
                intentAsal = null;
            }
            else{
                loadFragment(new BerandaFragment());
            }
        }else{
            loadFragment(new BerandaFragment());
        }

        item.setChecked(true);
    }

}
