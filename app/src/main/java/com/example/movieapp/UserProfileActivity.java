package com.example.movieapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.adapter.UserProfileFavoritesAdapter;
import com.example.movieapp.model.Favorites;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//10.08
public class UserProfileActivity extends AppCompatActivity {
    RecyclerView movieList;
    UserProfileFavoritesAdapter profileAdapter;
    ImageButton settingButton;
    TextView favMovieName;
    ImageView favMovieImg;
    //18.08 Gün Sonu
    TextView fullName, userName,email;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    String userID;
    FirebaseUser user;
    //19.08
    TextView verifyMsg;
    Button verifyEmailBtn;
    ImageView profilePhoto;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        settingButton = findViewById(R.id.setting_button);
        movieList = findViewById(R.id.userprofile_recycler);
        favMovieName = findViewById(R.id.favorites_movie_name);
        favMovieImg = findViewById(R.id.favorites_movie_img);
        //18.08 Gün Sonu
        fullName =findViewById(R.id.profile_fullname);
        userName = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        profilePhoto = findViewById(R.id.user_profil_photo);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profilePhoto));
        //19.08
        verifyMsg = findViewById(R.id.verify_msg);
        verifyEmailBtn = findViewById(R.id.verify_button);
        //19.08
        if(!user.isEmailVerified()) {
            verifyEmailBtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }
        verifyEmailBtn.setOnClickListener(view -> {
            //send verification email
            user.sendEmailVerification().addOnSuccessListener(unused -> {
                Toast.makeText(UserProfileActivity.this, "Doğrulama e-mailinize gönderildi.", Toast.LENGTH_LONG).show();
                verifyEmailBtn.setVisibility(View.GONE);
                verifyMsg.setVisibility(View.GONE);
            });
        });

/*
        DocumentReference documentReference = mStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fullName.setText(value.getString("fName"));
                userName.setText(value.getString("username"));
                email.setText(value.getString("email"));

            }
        });*/
        userInfo();
        setFavoritesRecycler();

    }
    private void userInfo(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    fullName.setText(user.getFullName());
                    userName.setText(user.getUserName());
                    email.setText(user.getEmail());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setFavoritesRecycler() {
        List<Favorites> favoritesList = new ArrayList<>();
        favoritesList.add(new Favorites("The Outlander","https://tr.web.img3.acsta.net/r_1280_720/pictures/14/05/09/11/16/481757.jpg"));
        favoritesList.add(new Favorites("The OA","https://i.pinimg.com/originals/b3/85/16/b3851673620461d1317a64833c5ad5e7.jpg"));
        favoritesList.add(new Favorites("Yıldızlararası","https://tr.web.img2.acsta.net/pictures/14/10/09/15/52/150664.jpg"));
        favoritesList.add(new Favorites("Teen Wolf","https://cdn.dsmcdn.com//ty10/product/media/images/20201015/18/16130237/93722623/1/1_org_zoom.jpg"));
        favoritesList.add(new Favorites("Sully","https://www.cocuklasinema.com/assets/admin/images/movie/0-sully.jpg"));
        favoritesList.add(new Favorites("Vis a Vis","https://tr.web.img4.acsta.net/r_1280_720/pictures/16/03/10/09/39/100264.jpg"));
        favoritesList.add(new Favorites("How I Met Your Mother","https://tr.web.img4.acsta.net/medias/nmedia/18/74/38/63/20215058.jpg"));
        favoritesList.add(new Favorites("Harry Potter ve Ateş Kadehi ","https://tr.web.img2.acsta.net/pictures/bzp/01/53756.jpg"));
        favoritesList.add(new Favorites("Babam ve Oğlum","https://tr.web.img3.acsta.net/pictures/13/12/09/21/04/046693.jpg"));
        favoritesList.add(new Favorites("Venom Zehirli Öfke 2","https://tr.web.img4.acsta.net/pictures/21/05/10/15/42/2170945.jpg"));


        profileAdapter = new UserProfileFavoritesAdapter(this,favoritesList);
        GridLayoutManager gridLayoutManager =new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        movieList.setLayoutManager(gridLayoutManager);
        movieList.setAdapter(profileAdapter);

        settingButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            Toast.makeText(getApplicationContext(),"Ayarlar Sayfasına Yönlendiriliyor",Toast.LENGTH_SHORT).show();
            intent.putExtra("fName",fullName.getText().toString());
            intent.putExtra("username",userName.getText().toString());
            intent.putExtra("email",email.getText().toString());

            startActivity(intent);

        });

        profilePhoto.setOnClickListener(v->{
            //open gallery
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,1000);

        });
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
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePhoto);
            }
        })).addOnFailureListener(e -> Toast.makeText(UserProfileActivity.this, "Hata!", Toast.LENGTH_LONG).show());
    }


}
