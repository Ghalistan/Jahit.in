package protel.jahitin.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import protel.jahitin.R;
import protel.jahitin.Utils.ProgressBarUtils;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private ImageView visibility;
    private TextView loginbtn;
    private Button regisbtn;
    private Toast mToast;
    private ProgressBar loadingIndicator;
    private EditText namaLengkapET, emailET, passwordET;
    private ProgressBarUtils pbUtils;

    private FirebaseAuth mAuth;
    //private DatabaseReference userDatabaseReference;

    boolean cekVisibility = false;

    public static final int MINIMAL_INPUT_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingIndicator = findViewById(R.id.pb_register);
        pbUtils = new ProgressBarUtils();

        namaLengkapET = findViewById(R.id.et_display_name);
        emailET = findViewById(R.id.et_email);
        passwordET = findViewById(R.id.et_password);
        visibility = findViewById(R.id.visibility);
        visibility.setOnClickListener(this);
        loginbtn = findViewById(R.id.login_button);
        loginbtn.setOnClickListener(this);
        regisbtn = findViewById(R.id.regis_button);
        regisbtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        //userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
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
            case R.id.login_button:
                Intent loginIntent = new Intent(Register.this, Beranda.class);
                finish();
                startActivity(loginIntent);
                break;
            case R.id.regis_button:
                hideSoftKeyboard();

                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String displayName = namaLengkapET.getText().toString();
                createAccount(email, password, displayName);
                break;
        }
    }

    public void createAccount(String email, String password, final String displayName){
        if(!validate()){
            return;
        }else{
            pbUtils.showLoadingIndicator(loadingIndicator);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                changeDisplayName(displayName);
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(!user.isEmailVerified()) {
                                    sendEmailVerification();
                                    createToast("Registrasi berhasil, silahkan verifikasi email anda");
                                }else{
                                    createToast("Registrasi berhasil, email anda sudah terverifikasi");
                                }

                                Intent intent = new Intent(Register.this, Beranda.class);
                                startActivity(intent);
                                finish();
                            }else{
                                createToast("Autentikasi gagal, silahkan coba lagi");
                            }

                            pbUtils.hideLoadingIndicator(loadingIndicator);
                        }
                    });
        }

    }

    public void sendEmailVerification(){
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                createToast("Gagal mengirimkan verifikasi email");
                            }else{

                            }
                        }
                    });
        }
    }

    public void changeDisplayName(String name){
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        if(user != null){
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(Register.class.getSimpleName(), "User profile updated.");
                            }
                        }
                    });
        }
    }


    public void createToast(String message){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public boolean validate(){
        if(namaLengkapET != null && emailET != null && passwordET != null){
            String nama = namaLengkapET.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();

           if(nama.isEmpty() || password.isEmpty() || email.isEmpty()){
                String message = "Lengkapi data anda terlebih dahulu";
                createToast(message);
                return false;
            }
            else if(nama.length() < MINIMAL_INPUT_LENGTH ||
                   password.length() < MINIMAL_INPUT_LENGTH)
            {
                String message = "Minimal jumlah karakter adalah 8";
                createToast(message);
                return false;
            }
        }else{
            String message = "Lengkapi data anda terlebih dahulu";
            createToast(message);
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
}
