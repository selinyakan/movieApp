package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    //ImageButton exitButton;
    Button exitButton;
    EditText profileFullName, profileEmail, profileUserName;
    ImageView profilePhoto;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    Button saveBtn;
    FirebaseUser user;
    StorageReference storageReference;
    //20.08
    //Button changePassword;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    DatabaseReference databaseRef;
    String fullName,userName,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //exitButton = findViewById(R.id.exit_button);
        exitButton = findViewById(R.id.logout);

        Intent data = getIntent();
        fullName=data.getStringExtra("fullName");
        userName=data.getStringExtra("userName");
        email=data.getStringExtra("email");

        //20.08
        reset_alert= new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();

        profileFullName = findViewById(R.id.settings_fullname);
        profileEmail = findViewById(R.id.settings_email);
        profileUserName = findViewById(R.id.settings_username);
        profilePhoto = findViewById(R.id.setting_pp);
        saveBtn = findViewById(R.id.setting_savebutton);
        //20.08
        //changePassword = findViewById(R.id.change_password);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");


        //bunu eklediğimizde bi önceki sayfadaki fotoğraf settingse de aktarıldı.
        StorageReference profileRef = storageReference.child("users/"+user.getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profilePhoto));

        exitButton.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        });

        /*
        exitButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        });
        */


        profilePhoto.setOnClickListener(v->{
            Toast.makeText(SettingsActivity.this, "Profil fotoğrafına tıklandı.", Toast.LENGTH_LONG).show();
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,1000);

        });

/*
        saveBtn.setOnClickListener(v->{

            if(profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profileUserName.getText().toString().isEmpty()){
                Toast.makeText(SettingsActivity.this, "Bir veya birçok alan boş.", Toast.LENGTH_SHORT).show();
                return;
            }
            final String mail = profileEmail.getText().toString();
            user.updateEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DocumentReference documentReference
                    Map<String,Object> edited = new HashMap<>();
                    edited.put("email",mail);
                    edited.put("fullName",profileFullName.getText().toString());
                    edited.put("userName",profileUserName.getText().toString());

                    documentReference.update(edited).addOnSuccessListener(unused1 -> Toast.makeText(SettingsActivity.this, "Profil Güncellendi.", Toast.LENGTH_SHORT).show());
                    Toast.makeText(SettingsActivity.this, "Kullanıcı Bilgileri Güncellendi.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }); */

        profileFullName.setText(fullName);
        profileUserName.setText(userName);
        profileEmail.setText(email);


        Log.d(TAG,"onCreate: "+ fullName + " " + userName );

        //20.08
        /*userID = user.getUid();
        user = mAuth.getCurrentUser();
        changePassword.setOnClickListener(v->{
            EditText resetPassword = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Şifre Yenile");
            passwordResetDialog.setMessage(">6 karakter uzunluğunda yeni şifre girin");
            passwordResetDialog.setView(resetPassword);

            passwordResetDialog.setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //extract the email send rset link
                    String newPassword = resetPassword.getText().toString();
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SettingsActivity.this, "Password Reset SUCCESFULLY.", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingsActivity.this, "Password Reset failed :(.", Toast.LENGTH_SHORT).show();

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



    }//OnCreate Kapanışı

    public void update(View v){
        if (isFullNameChanged()  || isEmailChanged() || isUserNameChanged()){
            Toast.makeText(SettingsActivity.this, "Profil Bilgileri Güncellendi.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SettingsActivity.this, "Bilgiler Güncellenemedi", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isFullNameChanged() {
        if(!fullName.equals(profileFullName.getText().toString())){
            databaseRef.child(mAuth.getUid()).child("fullName").setValue(profileFullName.getText().toString());
            fullName = profileFullName.getText().toString();
            return true;
        }else{
            return false;
        }
    }
 /*   private boolean isPasswordChanged() {
    }*/
    private boolean isEmailChanged() {
        if(!email.equals(profileEmail.getText().toString())){
            databaseRef.child(mAuth.getUid()).child("email").setValue(profileEmail.getText().toString());
            email = profileEmail.getText().toString();
            return true;
        }else{
            return false;
        }
    }
    private boolean isUserNameChanged() {
        if(!userName.equals(profileUserName.getText().toString())){
            databaseRef.child(mAuth.getUid()).child("userName").setValue(profileUserName.getText().toString());
            userName = profileUserName.getText().toString();
            return true;
        }else{
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profilePhoto.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);


            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        StorageReference fileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePhoto);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Hata!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()== R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        }
        if(item.getItemId()==R.id.deleteAccount){
            reset_alert.setTitle("Hesabı Kalıcı Olarak Sil")
                    .setMessage("Emin misiniz?")
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SettingsActivity.this, "Hesap Silindi", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).setNegativeButton("Vazgeç",null)
                    .create().show();

        }
        return super.onOptionsItemSelected(item);
    }


}