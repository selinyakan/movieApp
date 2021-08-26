package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText userPass,confirmPass;
    Button savePassBtn;
    FirebaseUser user;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userPass= findViewById(R.id.new_password);
        confirmPass=findViewById(R.id.confirm_password);
        savePassBtn =findViewById(R.id.reset_password_button);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();


        savePassBtn.setOnClickListener(v->{

            if(userPass.getText().toString().isEmpty()){
                userPass.setError("Gerekli Alan");
                return;
            }
            if(confirmPass.getText().toString().isEmpty()){
                confirmPass.setError("Gerekli Alan");
                return;
            }
            if (!userPass.getText().toString().equals(confirmPass.getText().toString())){
                confirmPass.setError("Parola eşleşmedi.");
            }
            FirebaseUser user = mAuth.getCurrentUser(); //IMPORTENT
            user.updatePassword(userPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ResetPasswordActivity.this, "Şifre Güncellendi.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}