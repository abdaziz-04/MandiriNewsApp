package com.mandiri.newsapp.network;

public class ApiUtils {

    public static final String BASE_URL = "https://newsapi.org/";

    public static NewsApiService getNewsApiService() {
        return RetrofitClient.getClient(BASE_URL).create(NewsApiService.class);
    }
}

