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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieapp.adapter.UserProfileFavoritesAdapter;
import com.example.movieapp.model.AllCategory;
import com.example.movieapp.model.CategoryItem;
import com.example.movieapp.model.Favorites;
import com.example.movieapp.model.MovieListBaseM;
import com.example.movieapp.model.SeriesListBaseM;
import com.example.movieapp.model.User;
import com.example.movieapp.restapi.IRest;
import com.example.movieapp.restapi.RetrofitPage;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//10.08
public class UserProfileActivity extends AppCompatActivity {
    RecyclerView movieList;
    UserProfileFavoritesAdapter profileAdapter;
    ImageButton settingButton;
    TextView favMovieName;
    ImageView favMovieImg;
    //18.08 Gün Sonu
    TextView fullName, userName, email,password;
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
        favMovieImg = findViewById(R.id.favorites_movie_img);
        //18.08 Gün Sonu
        fullName = findViewById(R.id.profile_fullname);
        userName = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        profilePhoto = findViewById(R.id.user_profil_photo);
        password = findViewById(R.id.password);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profilePhoto));
        //19.08
        verifyMsg = findViewById(R.id.verify_msg);
        verifyEmailBtn = findViewById(R.id.verify_button);
        //19.08
        if (!user.isEmailVerified()) {
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
        // transferMovie();
        // setFavoritesRecycler();

    }

    private void transferMovie(ArrayList<String> favoriteMovies2, ArrayList<String> favoriteMovies3) {
        ArrayList<String> link = new ArrayList<>();
        ArrayList<Boolean> link2 = new ArrayList<>();
        ArrayList<String> link3 = new ArrayList<>();
        for (int i = 0; i < favoriteMovies2.size(); i++) {
            Call<MovieListBaseM> call = prepareRetrofit().getMovieCredits(favoriteMovies2.get(i));
            int y=i;
            call.enqueue(new Callback<MovieListBaseM>() {

                @Override
                public void onResponse(Call<MovieListBaseM> call, Response<MovieListBaseM> response) {
                    MovieListBaseM result = response.body();


                    if (response.isSuccessful() && result != null) {
                        link.add(result.poster_path);
                        link2.add(true);
                        link3.add(favoriteMovies2.get(y));

                    }
                }

                @Override
                public void onFailure(Call<MovieListBaseM> call, Throwable t) {
                    Log.d("", "error catched at getPatientTCNo: " + t);
                }
            });
        }
        transferSeries(favoriteMovies3, link, favoriteMovies2, link2, link3);
    }

    private void transferSeries(ArrayList<String> favoriteMovies3, ArrayList<String> link, ArrayList<String> favoriteMovies2, ArrayList<Boolean> link2, ArrayList<String> link3) {
        favoriteMovies2.addAll(favoriteMovies3);
        for (int i = 0; i < favoriteMovies3.size(); i++) {
            Call<SeriesListBaseM> call = prepareRetrofit().getSeriesCredits(favoriteMovies3.get(i));
            int y=i;
            call.enqueue(new Callback<SeriesListBaseM>() {

                @Override
                public void onResponse(Call<SeriesListBaseM> call, Response<SeriesListBaseM> response) {
                    SeriesListBaseM result = response.body();


                    if (response.isSuccessful() && result != null) {
                        link.add(result.poster_path);
                        link2.add(false);
                        link3.add(favoriteMovies3.get(y));

                    }
                    setFavoritesRecycler(link, link2, link3);
                }

                @Override
                public void onFailure(Call<SeriesListBaseM> call, Throwable t) {
                    Log.d("", "error catched at getPatientTCNo: " + t);
                }
            });
        }
    }

    private void userInfo(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    fullName.setText(user.getFullName());
                    userName.setText(user.getUserName());
                    email.setText(user.getEmail());
                    password.setText(user.getPassword());
                    if(user.getFavoriteMovies() == null || user.getFavoriteTvSeries() == null){

                    }else{
                        transferMovie(user.getFavoriteMovies(),user.getFavoriteTvSeries());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    private void setFavoritesRecycler(ArrayList<String> link, ArrayList<Boolean> link2, ArrayList<String> link3) {
        List<Favorites> favoritesList = new ArrayList<>();

        for (int i=0; i<link.size(); i++){
            favoritesList.add(new Favorites("https://image.tmdb.org/t/p/w500" +link.get(i),
                    link2.get(i), link3.get(i)));
        }


        profileAdapter = new UserProfileFavoritesAdapter(UserProfileActivity.this,favoritesList);
        GridLayoutManager gridLayoutManager =new GridLayoutManager(UserProfileActivity.this,3,GridLayoutManager.VERTICAL,false);
        movieList.setLayoutManager(gridLayoutManager);
        movieList.setAdapter(profileAdapter);



        settingButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            Toast.makeText(getApplicationContext(),"Ayarlar Sayfasına Yönlendiriliyor",Toast.LENGTH_SHORT).show();
            intent.putExtra("fullName",fullName.getText().toString());
            intent.putExtra("userName",userName.getText().toString());
            intent.putExtra("email",email.getText().toString());
            intent.putExtra("password",password.getText().toString());


            startActivity(intent);

        });

        profilePhoto.setOnClickListener(v->{
            //open gallery
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,1000);

        });
    }

    public static IRest prepareRetrofit(){
        Retrofit retrofit = RetrofitPage.getRetrofit();
        return retrofit.create(IRest.class);
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