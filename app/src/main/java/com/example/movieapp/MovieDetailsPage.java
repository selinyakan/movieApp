package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieapp.adapter.CastAdapter;
import com.example.movieapp.adapter.GenreAdapter;
import com.example.movieapp.adapter.KanalAdapter;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Crew;
import com.example.movieapp.model.Flatrate;
import com.example.movieapp.model.Genre;
import com.example.movieapp.model.MovieListBaseM;
import com.example.movieapp.model.ProviderList;
import com.example.movieapp.model.SeriesListBaseM;
import com.example.movieapp.restapi.IRest;
import com.example.movieapp.restapi.RetrofitPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetailsPage extends AppCompatActivity {
    ImageView img, img2;
    TextView title, kategori, imdb, overview, directorName;
    private RecyclerView castRcy2, castRrcy3, genre_recycler;
    private CastAdapter castAdapter;
    ImageButton favoriButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initializeUIElements();
        String id = getIntent().getStringExtra("movieId");

        favoriButton.setOnClickListener(v->{
            FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

            if(favoriButton.getTag().equals("save")){
                FirebaseDatabase.getInstance().getReference("users")
                        .child(mAuth.getUid()).child("favorites").child(id).setValue(true);
            }else{
                FirebaseDatabase.getInstance().getReference("users")
                        .child(mAuth.getUid()).child("favorites").child(id).removeValue();
            }
        });

        setupRvCast(id);
        //setupRvCastSeries(id);
        //getSeriesList(id);
        getMovieList(id);
        getProviders(id);
        getDirector(id);
        //getDirectorSeries(id);
        getGenre(id);
        saveToList();
    }

    private void saveToList(){
        ArrayList<String> favoriteList = new ArrayList();
        String id = getIntent().getStringExtra("movieId");
        ImageButton movieLike = findViewById(R.id.favoriButton);
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference("users");
        saveRef.child(mAuth.getUid()).child("favorites").child(id);
        saveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()){
                    favoriButton.setImageResource(R.drawable.heart2);
                    movieLike.setTag("saved");
                }else{
                    movieLike.setImageResource(R.drawable.heart);
                    movieLike.setTag("save");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeUIElements() {
        img = findViewById(R.id.item_image);
        img2 = findViewById(R.id.detail_movie_cover);
        title = findViewById(R.id.detail_movie_title);
        kategori = findViewById(R.id.kategori);
        imdb = findViewById(R.id.imdb);
        overview = findViewById(R.id.overview);
        directorName = (TextView) findViewById(R.id.directorName);
        castRcy2 = findViewById(R.id.cast_recycler2);
        genre_recycler = findViewById(R.id.genre_rcycler);
        castRrcy3 = findViewById(R.id.cast_recycler3);
        favoriButton = findViewById(R.id.favoriButton);
    }

    private void getGenre(String id) {
        List<Genre> mData3 = new ArrayList<>();
        Call<MovieListBaseM> call = prepareRetrofit().getMovieCredits(id);
        call.enqueue(new Callback<MovieListBaseM>() {

            @Override
            public void onResponse(Call<MovieListBaseM> call, Response<MovieListBaseM> response) {
                MovieListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    for (int i=0;i<result.genres.size();i++){
                        mData3.add(new Genre(result.genres.get(i).name));

                    }
                    GenreAdapter genreAdapter = new GenreAdapter(MovieDetailsPage.this, mData3);
                    genre_recycler.setAdapter(genreAdapter);
                    genre_recycler.setLayoutManager(new LinearLayoutManager(MovieDetailsPage.this, LinearLayoutManager.HORIZONTAL, false));



                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getDirector(String id) {
        Call<MovieListBaseM> call = prepareRetrofit().getMovieCreditsCasts(id);
        call.enqueue(new Callback<MovieListBaseM>() {

            @Override
            public void onResponse(Call<MovieListBaseM> call, Response<MovieListBaseM> response) {
                MovieListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    directorName.setText(result.crew.get(0).original_name);
                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getDirectorSeries(String id) {
        Call<SeriesListBaseM> call = prepareRetrofit().getSeriesCreditsCasts(id);
        call.enqueue(new Callback<SeriesListBaseM>() {

            @Override
            public void onResponse(Call<SeriesListBaseM> call, Response<SeriesListBaseM> response) {
                SeriesListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    directorName.setText(result.crew.get(0).original_name);
                }
            }

            @Override
            public void onFailure(Call<SeriesListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getProviders(String id) {
        List<Flatrate> mData3 = new ArrayList<>();
        Call<ProviderList> call = prepareRetrofit().getMovieCreditsProviders(id);
        call.enqueue(new Callback<ProviderList>() {

            @Override
            public void onResponse(Call<ProviderList> call, Response<ProviderList> response) {
                ProviderList result = response.body();

                if(response.isSuccessful() && result != null)
                {
                   try {
                       if (result.results.TR.flatrate != null){
                           mData3.add(new Flatrate("https://image.tmdb.org/t/p/w500"+result.results.TR.flatrate.get(0).logo_path));
                       }
                       else if(result.results.TR.flatrate_and_buy != null){
                           mData3.add(new Flatrate("https://image.tmdb.org/t/p/w500"+result.results.TR.flatrate_and_buy.get(0).logo_path));
                       }
                       else if(result.results.TR.buy != null){
                           mData3.add(new Flatrate("https://image.tmdb.org/t/p/w500"+result.results.TR.buy.get(0).logo_path));
                       }
                       else if(result.results.TR.rent != null){
                           mData3.add(new Flatrate("https://image.tmdb.org/t/p/w500"+result.results.TR.rent.get(0).logo_path));
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                       //mData3.add(new Flatrate("https://cf.kizlarsoruyor.com/q10343625/235d76b8-b64c-4e0b-8f51-7f30543e43d2-m.jpg"));
                   }
                }
                    KanalAdapter kanalAdapter = new KanalAdapter(MovieDetailsPage.this, mData3);
                    castRrcy3.setAdapter(kanalAdapter);
                    castRrcy3.setLayoutManager(new LinearLayoutManager(MovieDetailsPage.this, LinearLayoutManager.HORIZONTAL, false));
                }

            @Override
            public void onFailure(Call<ProviderList> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });

    }

    private void setupRvCast(String id) {

        List<Cast> mData2 = new ArrayList<>();
        Call<MovieListBaseM> call = prepareRetrofit().getMovieCreditsCasts(id);
        call.enqueue(new Callback<MovieListBaseM>() {

            @Override
            public void onResponse(Call<MovieListBaseM> call, Response<MovieListBaseM> response) {
                MovieListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    for (int i=0;i<result.cast.size();i++){
                        mData2.add(new Cast(result.cast.get(i).original_name,
                                "https://image.tmdb.org/t/p/w500"+result.cast.get(i).profile_path));

                    }
                    castAdapter = new CastAdapter(MovieDetailsPage.this, mData2);
                    castRcy2.setAdapter(castAdapter);
                    castRcy2.setLayoutManager(new LinearLayoutManager(MovieDetailsPage.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });


    }

    private void setupRvCastSeries(String id) {

        List<Cast> mData2 = new ArrayList<>();
        Call<SeriesListBaseM> call = prepareRetrofit().getSeriesCreditsCasts(id);
        call.enqueue(new Callback<SeriesListBaseM>() {

            @Override
            public void onResponse(Call<SeriesListBaseM> call, Response<SeriesListBaseM> response) {
                SeriesListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    for (int i=0;i<result.cast.size();i++){
                        mData2.add(new Cast(result.cast.get(i).original_name,
                                "https://image.tmdb.org/t/p/w500"+result.cast.get(i).profile_path));

                    }
                    castAdapter = new CastAdapter(MovieDetailsPage.this, mData2);
                    castRcy2.setAdapter(castAdapter);
                    castRcy2.setLayoutManager(new LinearLayoutManager(MovieDetailsPage.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<SeriesListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });


    }

    private void getMovieList(String id){
        Call<MovieListBaseM> call = prepareRetrofit().getMovieCredits(id);
        call.enqueue(new Callback<MovieListBaseM>() {

            @Override
            public void onResponse(Call<MovieListBaseM> call, Response<MovieListBaseM> response) {
                MovieListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    Glide.with(MovieDetailsPage.this).load("https://image.tmdb.org/t/p/w500" +result.poster_path).into(img);
                    imdb.setText(String.valueOf(result.vote_average));
                    title.setText(result.title);
                    overview.setText(result.overview);
                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getSeriesList(String id2){
        Call<SeriesListBaseM> call = prepareRetrofit().getSeriesCredits(id2);
        call.enqueue(new Callback<SeriesListBaseM>() {

            @Override
            public void onResponse(Call<SeriesListBaseM> call, Response<SeriesListBaseM> response) {
                SeriesListBaseM result1 = response.body();

                if(response.isSuccessful() && result1 != null)
                {
                    Glide.with(MovieDetailsPage.this).load("https://image.tmdb.org/t/p/w500" +result1.poster_path).into(img);
                    imdb.setText(String.valueOf(result1.vote_average));
                    title.setText(result1.name);
                    overview.setText(result1.overview);
                }
            }

            @Override
            public void onFailure(Call<SeriesListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    public static IRest prepareRetrofit(){
        Retrofit retrofit = RetrofitPage.getRetrofit();
        return retrofit.create(IRest.class);
    }

    /*
    public void addToFavoriteMovie(View view, Context context, String movieId){
        String userID;
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        long timestamp = System.currentTimeMillis();
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = mStore.collection("favorites").document(movieId)
                .collection("movieLikes").document(userID);
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("movieId", "" + movieId);
        hashMap.put("timestamp",""+ timestamp);
                documentReference.set(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Listeye Eklendi", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void removeFromFavorite(Context context, String movieId){
    } */

     /*  favoriButton.setOnClickListener(v -> {
            Toast.makeText(this, "İlk Toast Mesajımız..", Toast.LENGTH_SHORT).show();
            //favoriButton.setImageResource(R.drawable.heart2);
            //MoviesPojo moviePoster = new MoviesPojo();
            FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites").child(mAuth.getUid());

            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(mAuth.getUid()).exists()){
                        favoriButton.setImageResource(R.drawable.heart);
                        favoriButton.setTag("liked");
                    }else{
                        favoriButton.setImageResource(R.drawable.heart2);
                        favoriButton.setTag("like");
                        FirebaseDatabase.getInstance().getReference().child("favorites").child(mAuth.getUid())
                                .child(id).setValue(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }); */
}
