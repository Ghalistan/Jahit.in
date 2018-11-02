package protel.jahitin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.aakira.expandablelayout.Utils;

import protel.jahitin.R;

public class SplashScreen extends AppCompatActivity {
    Intent intent;
    private Boolean firstTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFirstTime()) {
            intent = new Intent(SplashScreen.this, onBoarding.class);
            startActivity(intent);
        } else {
            intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
        }
    }

    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreference = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreference.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreference.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }
}
