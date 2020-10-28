package com.example.newfeedapp.api;

import com.example.newfeedapp.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News>  getNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
