package protel.jahitin.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import protel.jahitin.R;

public class Login extends AppCompatActivity implements View.OnClickListener {
    ImageView visibility;
    TextView forgot_password, tvRegister;
    EditText password;
    Button login_btn;
    boolean cekVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        visibility = findViewById(R.id.visibility);
        visibility.setOnClickListener(this);
        password = findViewById(R.id.password_input);
        login_btn = findViewById(R.id.login_button);
        login_btn.setOnClickListener(this);
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
        tvRegister = findViewById(R.id.register_button);
        tvRegister.setOnClickListener(this);
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
            case R.id.register_button:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                Intent intent1 = new Intent(Login.this, Beranda.class);
                startActivity(intent1);
                break;
        }
    }
}
