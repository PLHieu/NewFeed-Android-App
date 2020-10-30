package com.example.newfeedapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.newfeedapp.api.ApiClient;
import com.example.newfeedapp.api.ApiInterface;
import com.example.newfeedapp.model.Article;
import com.example.newfeedapp.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "658ec996e2ed45308c2f8964b8024b98";
    private RecyclerView _recyclerView;
    private RecyclerView.LayoutManager _layoutManager;
    private List<Article> _articles = new ArrayList<>();
    private Adapter _adapter    ;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _recyclerView = findViewById(R.id.recyclerview);
        _layoutManager = new LinearLayoutManager(MainActivity.this);
        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setNestedScrollingEnabled(false);

        LoadJson();
    }

    public void LoadJson(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

//        String country = Utils.getCountry();
        String country = "us";

        Call<News> call;
        call = apiInterface.getNews(country, API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    if(!_articles.isEmpty()){
                        _articles.clear();
                    }
                    _articles = response.body().getArticles();
                    _adapter = new Adapter(_articles, MainActivity.this);
                    _recyclerView.setAdapter(_adapter);
                    _adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }
}