package com.example.newfeedapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

        LoadJson("");
    }

    public void LoadJson(final String keyword){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

//        String country = Utils.getCountry();
        String country = "us";
        String language = "en";

        Call<News> call;

        if(keyword.length() > 0){
            call = apiInterface.getNewsSearch(keyword,API_KEY);
        }else{
            call = apiInterface.getNews(country, API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            // ta define Response<News> response tuc la respone o dang New, tuc la phan body se duoc auto parse sang dang News
            // do do cac tt trong class News phai de @SerializedName("status") @Expose
            // Tuy nhien trong New lai co List<Article> , do do thuoc tinh trong class Article cung phai de tuong tu
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager  searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 2){
                    LoadJson(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadJson(newText);
                return true;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);

//        return super.onCreateOptionsMenu(menu);
        return true;
    }
}