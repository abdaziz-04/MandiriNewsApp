package com.mandiri.newsapp.network;

import com.mandiri.newsapp.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    String API_KEY = "494e087972174d6ea67a3ae3ac51d25f";

    @GET("v2/top-headlines?country=id")
    Call<NewsResponse> getTopHeadlines(
            @Query("apiKey") String apiKey,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
