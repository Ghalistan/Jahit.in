package protel.jahitin.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private ImageView visibility;
    private TextView forgot_password, tvRegister;
    private EditText passwordET, emailET;
    private Button login_btn;
    private boolean cekVisibility = false;
    private ProgressBar progressBar;
    private ProgressBarUtils pbUtils;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        visibility = findViewById(R.id.visibility);
        visibility.setOnClickListener(this);
        emailET = findViewById(R.id.et_email_login);
        passwordET = findViewById(R.id.password_input);
        login_btn = findViewById(R.id.login_button);
        login_btn.setOnClickListener(this);
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
        tvRegister = findViewById(R.id.register_button);
        tvRegister.setOnClickListener(this);
        progressBar = findViewById(R.id.pb_login);

        pbUtils = new ProgressBarUtils();

    }

    private void setVisibility() {
        if (!cekVisibility) {
            visibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            cekVisibility = true;
        } else {
            visibility.setImageResource(R.drawable.ic_visibility_black_24dp);
            passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
                Intent registerIntent = new Intent(Login.this, Register.class);
                finish();
                startActivity(registerIntent);
                break;
            case R.id.login_button:
                hideSoftKeyboard();

                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if(validate()){
                    signIn(email, password);
                }
                break;
        }
    }

    public void signIn(String email, String password){
        pbUtils.showLoadingIndicator(progressBar);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("Login", "Success");
                            mUser = mAuth.getCurrentUser();
                            checkEmailVerified(mUser);
                        }else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }

                        pbUtils.hideLoadingIndicator(progressBar);
                    }
                });
    }

    public void checkEmailVerified(FirebaseUser user){
        if(user.isEmailVerified()){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(this, "Verifikasi email anda terlebih dahulu", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public boolean validate(){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            String message = "Lengkapi data anda terlebih dahulu";
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    public void hideSoftKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager) this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
