package com.example.movieapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.PushIdGenerator;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText fullName,userName,email, password;
    //Button signIn;
    Button signUp;
    TextView signIn;
    //17.08
    FirebaseAuth mAuth;
    //18.08
    FirebaseFirestore mStore;
    //18.08
    String userID;
    //24.08RealTimeDatabase
    DatabaseReference mRef;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullName = findViewById(R.id.register_fullname);
        userName = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        signIn = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.registerButton);
        //17.08
        mAuth = FirebaseAuth.getInstance();
        //18.08
        mStore = FirebaseFirestore.getInstance();
        //24.08RealTimeDatabase
        mRef= FirebaseDatabase.getInstance().getReference();

        /*
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            finish();
        } */

      /*  signIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }); */
        //24.08 //CloudFirestore register
         /*
        signUp.setOnClickListener(v -> {
          //17.08
            String mEmail = email.getText().toString().trim();
            String mPassword = password.getText().toString().trim();
            //18.08
            String mFullName = fullName.getText().toString().trim();
            String mUserName = userName.getText().toString().trim();

            if(TextUtils.isEmpty(mEmail)){
                email.setError("Email boş geçilemez.");
                return;
            }
            if(TextUtils.isEmpty(mPassword)){
                password.setError("Şifre gereklidir");
                return;
            }
            if(mPassword.length() < 6){
                password.setError("Şifre en az 6 karakter olmalıdır.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener((task -> {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Kullanıcı Oluşturuldu",Toast.LENGTH_LONG).show();
                    //users collection
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = mStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("fName",mFullName);
                    user.put("email",mEmail);
                    user.put("username",mUserName);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"BASARILI: Kullanıcı profili olusturuldu." + userID);
                        }
                    });
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Error! " +task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }));

            //register the user in firebase
            /*mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Kullanıcı Oluşturuldu",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),HomePage.class));
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this,"Error! " +task.getException().getMessage(),Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

        }); */

    }
    public void loginButton(View v){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }


    //RealTimeDatabase register
    public void register(View view){

        String mEmail = email.getText().toString().trim();
        String mPassword = password.getText().toString().trim();
        String mFullName = fullName.getText().toString().trim();
        String mUserName = userName.getText().toString().trim();
        ArrayList<String> favoriteMovie =  new ArrayList<>();
        ArrayList<String> favoriteTvSeries =  new ArrayList<>();


        if(!TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mFullName) && !TextUtils.isEmpty(mUserName)){
            mAuth.createUserWithEmailAndPassword(mEmail,mPassword)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user= mAuth.getCurrentUser(); //importent

                                mRef.child("users").child(mAuth.getUid()).setValue(new User(mAuth.getUid(),mEmail ,mFullName,
                                        mPassword,mUserName,favoriteMovie,favoriteTvSeries));


                                Toast.makeText(RegisterActivity.this, "Kayıt İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),HomePage.class));
                                /*mRef.child("users");
                                Map<String,Object> mData = new HashMap<>();
                                mData.put(userID, new User(userID,mEmail ,mFullName,
                                        mPassword,mUserName));
                                mRef.setValue(mData);
                                Toast.makeText(RegisterActivity.this, "Kayıt İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),HomePage.class));*/
                                /*
                                mData.put("userId", userID);
                                mData.put("fullName",mFullName);
                                mData.put("email",mEmail);
                                mData.put("userName",mUserName.toLowerCase());
                                mData.put("password",mPassword);
                                mData.put("favorites",favoriteList);*/
                                /*
                                mRef.child("users").child(mAuth.getUid())
                                        .setValue(mData)
                                        .addOnCompleteListener(RegisterActivity.this, task1 -> {
                                            if(task1.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "Kayıt İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),HomePage.class));

                                            }else{
                                                Toast.makeText(RegisterActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }); */
                            }else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this, "Email ve Şifre Boş Olamaz", Toast.LENGTH_SHORT).show();
        }
    }
}