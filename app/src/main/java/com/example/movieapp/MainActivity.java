package com.example.movieapp;
//Denemeeeeeeeeeeeeeeeeeeeeeeeeee
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    TextView forgetPassword;
    Button signIn, signUp;
    //17.08
    FirebaseAuth mAuth;
    //20.08
    FirebaseUser user;
    //24.08
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        //17.08
        forgetPassword = findViewById(R.id.forget_password);
        signIn = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.registerButton);
        //17.08
        mAuth = FirebaseAuth.getInstance();
        //24.08
        mRef= FirebaseDatabase.getInstance().getReference();
        //20.08
        //userID = user.getUid();
        //user = mAuth.getCurrentUser();

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

        });

    }
    //CloudFirestore Database
    /*
    public void login(View view){
        if(email.getText().toString().isEmpty()){
            email.setError("Email eksik.");
            return;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Şifre eksik.");
            return;
        }
        //data is valid
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //login is successful
                startActivity(new Intent(getApplicationContext(),HomePage.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    } */

    public void forgetPass(View view){
        EditText resetMail = new EditText(view.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Şifreni Sıfırlamak İster misin?");
        passwordResetDialog.setMessage("Sıfırlama bağlantısı için lütfen mailini gir :)");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Gönder", (dialogInterface, i) -> {
            //extract the email send rset link
            String mail = resetMail.getText().toString();
            mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this,"Sıfırlama bağlantısı emailinize gönderildi.",Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(e -> Toast.makeText(MainActivity.this,"Hata! Sıfırlama bağlantısı gönderilemedi." +e.getMessage(),Toast.LENGTH_LONG).show());
        });
        passwordResetDialog.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close the dialog

            }
        });
        passwordResetDialog.create().show();
    }

    //RealTime Database login
    public void login(View view){
        String mEmail = email.getText().toString().trim();
        String mPassword = password.getText().toString().trim();
        if(!TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)){
            mAuth.signInWithEmailAndPassword(mEmail,mPassword)
                    .addOnSuccessListener(MainActivity.this, authResult -> {
                        user= mAuth.getCurrentUser();
                        mRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                startActivity(new Intent(getApplicationContext(),HomePage.class));
                                finish();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }else {
            Toast.makeText(MainActivity.this, "Email ve Şifre Boş Olamaz", Toast.LENGTH_SHORT).show();
        }

    }

    //kullanıcı daha önce giriş yapmışsa hemen anasayfaya yönlendiriyor

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }
    }
}


/*
        signIn.setOnClickListener(v -> {
            //18.07
            //extract and validate //24.08 deneme

            if(email.getText().toString().isEmpty()){
                email.setError("Email eksik.");
                return;
            }
            if(password.getText().toString().isEmpty()){
                password.setError("Şifre eksik.");
                return;
            }
            //data is valid
            mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //login is successful
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
            //login user

        }); */
/*
        forgetPassword.setOnClickListener(v->{
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Şifreni Sıfırlamak İster misin?");
            passwordResetDialog.setMessage("Sıfırlama bağlantısı için lütfen mailini gir :)");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //extract the email send rset link
                    String mail = resetMail.getText().toString();
                    mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this,"Sıfırlama bağlantısı emailinize gönderildi.",Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Hata! Sıfırlama bağlantısı gönderilemedi." +e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });
            passwordResetDialog.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //close the dialog

                }
            });
            passwordResetDialog.create().show();
        }); */