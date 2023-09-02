package com.mandiri.newsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
    private int currentPage = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, this);
        recyclerView.setAdapter(newsAdapter);

        // Set up infinite scrolling
        setupScrollListener();

        // Load initial news
        loadNews();
    }

    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    currentPage++;
                    loadMoreNews();
                    isLoading = true;
                }
            }
        });
    }

    private void loadNews() {
        progressBar.setVisibility(View.VISIBLE);

        NewsApiService newsApiService = ApiUtils.getNewsApiService();
        Call<NewsResponse> call = newsApiService.getTopHeadlines(
                NewsApiService.API_KEY,
                currentPage,
                10
        );

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<News> newsList = newsResponse.getArticles();
                    if (currentPage == 1 && newsList.size() > 0) {
                        // Set berita terbaru jika tersedia
                        newsAdapter.setLatestNews(newsList.get(0));
                        newsList.remove(0); // Hapus berita terbaru dari daftar
                    }
                    newsAdapter.addMoreData(newsList); // Tambahkan sisanya ke RecyclerView
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

    private void loadMoreNews() {
        progressBar.setVisibility(View.VISIBLE);

        NewsApiService newsApiService = ApiUtils.getNewsApiService();
        Call<NewsResponse> call = newsApiService.getTopHeadlines(
                NewsApiService.API_KEY,
                currentPage,
                10
        );

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<News> newsList = newsResponse.getArticles();
                    newsAdapter.addMoreData(newsList);
                } else {
                    Log.e("API Error", response.message());
                }
                progressBar.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                progressBar.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    @Override
    public void onItemClick(News news) {
        // Tindakan yang akan diambil ketika item diklik
    }
}
