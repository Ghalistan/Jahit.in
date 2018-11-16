package protel.jahitin.Fragment;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import protel.jahitin.Activity.Beranda;
import protel.jahitin.Activity.Register;
import protel.jahitin.Model.User;
import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class AkunFragment extends Fragment implements View.OnClickListener{
    ImageView editData;
    private String nama, email, alamat;
    public TextView headernameTV, usernameTV, emailTV, alamatTV;
    private Button btnLogout;

    public FirebaseUser mUser;
    private FirebaseAuth mAuth;
    public DatabaseReference userRef;
    private ValueEventListener userValue;
    public User user;

    public AkunFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_akun, container, false);
        editData = view.findViewById(R.id.edit_datadiri);
        editData.setOnClickListener(this);
        headernameTV = view.findViewById(R.id.akun_username);
        usernameTV = view.findViewById(R.id.user_nama);
        emailTV = view.findViewById(R.id.email_user);
        alamatTV = view.findViewById(R.id.alamat_user);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        usernameTV.setText(mUser.getDisplayName());
        headernameTV.setText(mUser.getDisplayName());
        emailTV.setText(mUser.getEmail());

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        user = new User();
        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("user").child(mUser.getUid());
        attachDatabaseReadListener();

        return view;
    }

    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.item_alert_dialog_akun, null));
        builder.setMessage("Edit Data Diri");
        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText dialogNamaET = ((AlertDialog) dialog).findViewById(R.id.dialog_nama_lengkap);
                EditText dialogAlamatET = ((AlertDialog) dialog).findViewById(R.id.dialog_alamat);

                nama = dialogNamaET.getText().toString();
                alamat = dialogAlamatET.getText().toString();
                updateProfile(nama, alamat);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_datadiri:
                alertDialog();
                break;
            case R.id.btn_logout:
                buildLogoutAlert();
                break;
        }
    }

    private static int dpToPx(int dp) {
        return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void attachDatabaseReadListener(){
        if(userValue == null){
            userValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.hasChild("alamat")){
                        user.setAlamat(currUser.getAlamat());
                        alamatTV.setText(currUser.getAlamat());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            userRef.addValueEventListener(userValue);
        }
    }

    private void detachDatabaseListener(){
        if(userValue != null){
            userRef.removeEventListener(userValue);
            userValue = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    private void buildLogoutAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Logout dari Jahitin?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateProfile(String nama, String alamat){
        if(!nama.isEmpty()) {
            mUser.updateProfile(new UserProfileChangeRequest.Builder()
                    .setDisplayName(nama).build())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            usernameTV.setText(mUser.getDisplayName());
                            headernameTV.setText(mUser.getDisplayName());
                        }
                    });
        }

        if(!alamat.isEmpty()){
            user.setAlamat(alamat);
            Map<String, Object> mapUser = new HashMap<>();
            mapUser.put("alamat", alamat);

            userRef.updateChildren(mapUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    alamatTV.setText(user.getAlamat());
                }
            });
        }
    }
}
