package com.mandiri.newsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mandiri.newsapp.R;
import com.mandiri.newsapp.model.News;
import com.mandiri.newsapp.model.NewsResponse;
import com.mandiri.newsapp.network.ApiUtils;
import com.mandiri.newsapp.network.NewsApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, this);
        recyclerView.setAdapter(newsAdapter);

        loadNews();
    }

    private void loadNews() {
        NewsApiService newsApiService = ApiUtils.getNewsApiService();
        Call<NewsResponse> call = newsApiService.getTopHeadlines(
                NewsApiService.API_KEY,
                1,
                10
        );

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<News> newsList = newsResponse.getArticles();
                    newsAdapter.setData(newsList);
                } else {
                    Log.e("API Error", response.message());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(News news) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("news", news);
        startActivity(intent);
    }
}
