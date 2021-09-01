package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.movieapp.adapter.HomeRecyclerAdapter;
import com.example.movieapp.adapter.SliderMoviesAdapter;
import com.example.movieapp.model.AllCategory;
import com.example.movieapp.model.CategoryItem;
import com.example.movieapp.model.MovieListBaseM;
import com.example.movieapp.model.SeriesListBaseM;
import com.example.movieapp.model.SliderMovies;
import com.example.movieapp.model.User;
import com.example.movieapp.restapi.IRest;
import com.example.movieapp.restapi.RetrofitPage;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePage extends AppCompatActivity {
    SliderMoviesAdapter sliderMoviesAdapter;
    TabLayout indicatorTab,categoryTab;
    ViewPager sliderMoviesViewPager;
    List<SliderMovies> homeSliderList;
    //06.08
    List<SliderMovies> seriesSliderList;
    List<SliderMovies> filmsSliderList;
    Timer sliderTimer;
    NestedScrollView nestedScrollView;
    AppBarLayout appBarLayout;
    FirebaseUser user;
    FirebaseAuth mAuth;
    String userID;
    //06.08
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView homeRecycler;

    List<AllCategory> allCategoryList = new ArrayList<>();
    List<AllCategory> seriesAllCategoryList = new ArrayList<>();
    List<AllCategory> filmAllCategoryList = new ArrayList<>();

    StorageReference storageReference;


    //10.08
    private ImageView profilePhoto;
    private SearchView search;

    private void initializeUIElements(){
        indicatorTab = findViewById(R.id.tab_indicator);
        categoryTab = findViewById(R.id.homeOption);
        homeRecycler = findViewById(R.id.cast_recycler);
        nestedScrollView = findViewById(R.id.nestedScroll);
        appBarLayout = findViewById(R.id.appbar);
        search = findViewById(R.id.src);
        profilePhoto = findViewById(R.id.profile_photo_img);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        storageReference = FirebaseStorage.getInstance().getReference();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initializeUIElements();
        //getGenreHome();
        prepareDatas();
        setOnClickListener();
        getFunction();
        setSliderMoviesAdapter(homeSliderList);
        setHomeRecycler(allCategoryList);

        StorageReference profileRef = storageReference.child("users/"+user.getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profilePhoto));

    }

    private void getFunction() {
        for(int i = 1; i<10; i++){
            getMovies(String.valueOf(i));
        }

        for(int i = 1; i<10; i++){
            getSeries(String.valueOf(i));
        }
        for(int i = 1; i<3; i++){
            Random r=new Random(); //random sınıfı
            int a=r.nextInt(40);
            getPopularMoviesList(String.valueOf(a), i);
        }

    }

    private void getPopularMoviesList(String pageNumber, int i) {
        Call<MovieListBaseM> call6 = prepareRetrofit().getPopularMovieList(pageNumber);
        call6.enqueue(new Callback<MovieListBaseM>() {
            @Override
            public void onResponse(Call<MovieListBaseM> call6, Response<MovieListBaseM> response) {
                MovieListBaseM result1 = response.body();
                String [] genre = new String[3];
                genre[0] = "";
                genre[1] = "Önerilenler";
                genre[2] = "Popülerler";

                if(response.isSuccessful() && result1 != null)
                {
                    List<CategoryItem> data1 = new ArrayList<>();
                    for (int i=0;i<result1.results.size(); i++){
                        data1.add(new CategoryItem("https://image.tmdb.org/t/p/w500" +result1.results.get(i).poster_path,
                                result1.results.get(i).title, result1.results.get(i).id,true));

                    }
                    allCategoryList.add(new AllCategory(i, "*"+ genre[i].toString(), data1));
                    setHomeRecycler(allCategoryList);
                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call6, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getGenreHome(){
        user = mAuth.getCurrentUser();
        DatabaseReference recomRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        recomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if(user.getFavoriteMovies() == null){
                        for(int i = 1; i<3; i++){
                            Random r=new Random(); //random sınıfı
                            int a=r.nextInt(40);
                            getPopularMoviesList(String.valueOf(a), i);
                        }
                    }else{
                        // transferMovie(user.getFavoriteMovies());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    private void prepareDatas(){
        homeSliderList = new ArrayList<>();
        homeSliderList.add(new SliderMovies(1,"Venom","https://www.comicbookmovie.com/images/articles/banners/163458.jpg",""));
        homeSliderList.add(new SliderMovies(2,"The Godfather","https://film-book.com/wp-content/uploads/2020/11/the-godfather-coda-the-death-of-michael-corleone-movie-poster-banner-01-700x400-1.jpg",""));
        homeSliderList.add(new SliderMovies(3,"Dark","https://mainstreetartscs.org/wp-content/uploads/2020/04/dark.jpg",""));
        setSliderMoviesAdapter(homeSliderList);
        //06.08
        filmsSliderList = new ArrayList<>();
        filmsSliderList.add(new SliderMovies(1,"Divergent","https://www.absurdizi.com/wp-content/uploads/2019/10/divergent1.jpg",""));
        filmsSliderList.add(new SliderMovies(2,"Gora","https://i.sozcu.com.tr/wp-content/uploads/2019/03/iecrop/gora_16_9_1552404921.jpg",""));
        filmsSliderList.add(new SliderMovies(3,"Zindan Adası","https://i.pinimg.com/originals/df/09/f1/df09f10cbf94c95f217c8c0fbe7596a6.png",""));

        //06.08
        seriesSliderList = new ArrayList<>();
        seriesSliderList.add(new SliderMovies(1,"The Witcher","https://kwinnpop.com/wp-content/uploads/2019/12/The-Witcher-Banner.jpg",""));
        seriesSliderList.add(new SliderMovies(2,"Friends","https://blog.lidyana.com/wp-content/uploads/2019/03/friends.jpg",""));
        seriesSliderList.add(new SliderMovies(3,"La Casa De Papel","https://guiltybit.com/wp-content/uploads/2019/07/Critica-la-casa-de-papel-3.jpg",""));
    }

    private void setOnClickListener(){
        profilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            Toast.makeText(getApplicationContext(),"Profil Sayfasına Yönlendiriliyor",Toast.LENGTH_SHORT).show();
            startActivity(intent);

        });
        search.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchPage.class);
            Toast.makeText(getApplicationContext(),"Arama Sayfasına Yönlendiriliyor",Toast.LENGTH_SHORT).show();
            startActivity(intent);

        });

        categoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        setScrollDefaultState();
                        setSliderMoviesAdapter(filmsSliderList);
                        setHomeRecycler(filmAllCategoryList);
                        return;
                    case 2:
                        setScrollDefaultState();
                        setSliderMoviesAdapter(seriesSliderList);
                        setHomeRecycler(seriesAllCategoryList);
                        return;
                    default:
                        setScrollDefaultState();
                        setSliderMoviesAdapter(homeSliderList);
                        setHomeRecycler(allCategoryList);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private void getMovies(String pageNumber){
        Call<MovieListBaseM> call = prepareRetrofit().getMovieList(pageNumber);
        call.enqueue(new Callback<MovieListBaseM>() {
            @Override
            public void onResponse(Call<MovieListBaseM> call, Response<MovieListBaseM> response) {
                MovieListBaseM result = response.body();

                if(response.isSuccessful() && result != null)
                {
                    List<CategoryItem> data = new ArrayList<>();

                    for (int i=0;i<result.results.size(); i++){
                        data.add(new CategoryItem("https://image.tmdb.org/t/p/w500" +result.results.get(i).poster_path
                                ,result.results.get(i).title, result.results.get(i).id,true));
                    }

                    filmAllCategoryList.add(new AllCategory(Integer.parseInt(pageNumber), "", data));
                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getPopularMovies(String pageNumber, int i){
        Call<MovieListBaseM> call6 = prepareRetrofit().getPopularList(pageNumber);
        call6.enqueue(new Callback<MovieListBaseM>() {
            @Override
            public void onResponse(Call<MovieListBaseM> call6, Response<MovieListBaseM> response) {
                MovieListBaseM result1 = response.body();
                String [] genre = new String[2];
                genre[0] = "";
                genre[1] = "Önerilenler";

                if(response.isSuccessful() && result1 != null)
                {
                    List<CategoryItem> data1 = new ArrayList<>();
                    for (int i=0;i<result1.results.size(); i++){
                        data1.add(new CategoryItem("https://image.tmdb.org/t/p/w500" +result1.results.get(i).poster_path,
                                result1.results.get(i).title, result1.results.get(i).id,true));

                    }
                    allCategoryList.add(new AllCategory(i, "*"+ genre[i].toString(), data1));
                    setHomeRecycler(allCategoryList);
                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM> call6, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    private void getSeries(String pageNumber){
        Call<SeriesListBaseM> call6 = prepareRetrofit().getSeriesList(pageNumber);
        call6.enqueue(new Callback<SeriesListBaseM>() {
            @Override
            public void onResponse(Call<SeriesListBaseM> call6, Response<SeriesListBaseM> response) {
                SeriesListBaseM result1 = response.body();

                if(response.isSuccessful() && result1 != null)
                {
                    List<CategoryItem> data1 = new ArrayList<>();
                    for (int i=0;i<result1.results.size(); i++){
                        data1.add(new CategoryItem("https://image.tmdb.org/t/p/w500" +result1.results.get(i).poster_path,
                                result1.results.get(i).title, result1.results.get(i).id,false));

                    }

                    seriesAllCategoryList.add(new AllCategory(Integer.parseInt(pageNumber), "", data1));
                }
            }

            @Override
            public void onFailure(Call<SeriesListBaseM> call6, Throwable t) {
                Log.d("","error catched at getPatientTCNo: "+t);
            }
        });
    }

    public static IRest prepareRetrofit(){
        Retrofit retrofit = RetrofitPage.getRetrofit();
        return retrofit.create(IRest.class);
    }

    private void setSliderMoviesAdapter(List<SliderMovies> sliderMoviesList){
        sliderMoviesViewPager = findViewById(R.id.sliderViewPager);
        sliderMoviesAdapter = new SliderMoviesAdapter(this, sliderMoviesList);
        sliderMoviesViewPager.setAdapter(sliderMoviesAdapter);
        indicatorTab.setupWithViewPager(sliderMoviesViewPager);
        //06.08 sliderı otomatikleştirme
        Timer sliderTimer = new Timer();
        sliderTimer.scheduleAtFixedRate(new AutoSlider(),4000,6000);
        indicatorTab.setupWithViewPager(
                sliderMoviesViewPager,
                true
        );

    }

    class AutoSlider extends TimerTask{
        @Override
        public void run() {
            HomePage.this.runOnUiThread(() -> {

                if(sliderMoviesViewPager.getCurrentItem()< homeSliderList.size() - 1){
                    sliderMoviesViewPager.setCurrentItem(sliderMoviesViewPager.getCurrentItem() + 1);
                }else{
                    sliderMoviesViewPager.setCurrentItem(0);
                }
            });

        }
    }

    //AllCategory kategorileri temsil ediyor
    public void setHomeRecycler(List<AllCategory> allCategoryList){
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        homeRecycler.setLayoutManager(layoutManager);
        homeRecyclerAdapter = new HomeRecyclerAdapter(this,allCategoryList);
        homeRecycler.setAdapter(homeRecyclerAdapter);

    }


    private void setScrollDefaultState(){
        nestedScrollView.fullScroll(View.FOCUS_UP);
        nestedScrollView.scrollTo(0,0);
        appBarLayout.setExpanded(true);

    }

}