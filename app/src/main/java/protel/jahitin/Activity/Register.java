package protel.jahitin.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import protel.jahitin.R;

public class Register extends AppCompatActivity implements View.OnClickListener {
    ImageView visibility;
    EditText password;
    TextView loginbtn;

    boolean cekVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        visibility = findViewById(R.id.visibility);
        visibility.setOnClickListener(this);
        password = findViewById(R.id.password_input);
        loginbtn = findViewById(R.id.login_button);
        loginbtn.setOnClickListener(this);
    }

    private void setVisibility() {
        if (!cekVisibility) {
            visibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            cekVisibility = true;
        } else {
            visibility.setImageResource(R.drawable.ic_visibility_black_24dp);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            cekVisibility = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.visibility:
                setVisibility();
                break;
            case R.id.login_button:
                finish();
                break;
        }
    }
}
