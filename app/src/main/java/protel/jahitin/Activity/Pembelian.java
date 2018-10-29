package protel.jahitin.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import protel.jahitin.Fragment.BerandaFragment;
import protel.jahitin.R;

public class Pembelian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian);


        Intent intentAsal = getIntent();
        if(intentAsal.hasExtra(BerandaFragment.EXTRA_NAMA_TOKO)){
            //Log.d(Pembelian.class.getSimpleName(), intentAsal.getStringExtra(BerandaFragment.EXTRA_NAMA_TOKO));
            String namaToko = intentAsal.getStringExtra(BerandaFragment.EXTRA_NAMA_TOKO);
            getSupportActionBar().setTitle(namaToko);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }
}
