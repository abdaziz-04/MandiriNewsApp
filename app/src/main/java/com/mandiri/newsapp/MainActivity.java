package com.mandiri.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList = new ArrayList<>();
    private int currentPage = 1;
    private NewsApiService newsApiService;
    private String apiKey = "YOUR_NEWSAPI_KEY"; // Ganti dengan kunci API Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.newsRecyclerView);
        newsAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(newsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsApiService = retrofit.create(NewsApiService.class);

        loadHeadline(apiKey);
        loadNews(apiKey, currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        currentPage++;
                        loadNews(apiKey, currentPage);
                    }
                }
            }
        });
    }

    private void loadHeadline(String apiKey) {
        Call<NewsResponse> headlineCall = newsApiService.getTopHeadlines(apiKey, 1, 1);
        headlineCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    News headlineNews = response.body().getArticles().get(0);
                    TextView headlineTitle = findViewById(R.id.headlineTitle);
                    headlineTitle.setText(headlineNews.getTitle());

                    LinearLayout headlineLayout = findViewById(R.id.headlineLayout);
                    headlineLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load headline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNews(String apiKey, int page) {
        Call<NewsResponse> call = newsApiService.getTopHeadlines(apiKey, page, 10);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<News> newArticles = response.body().getArticles();
                    newsList.addAll(newArticles);
                    newsAdapter.notifyItemRangeInserted(newsList.size() - newArticles.size(), newArticles.size());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
