package com.mandiri.newsapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mandiri.newsapp.R;
import com.mandiri.newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CARD = 1;
    private static final int TYPE_LIST = 2;

    private List<News> newsList = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;
    private News latestNews;

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    public NewsAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    // Set data for the latest news
    public void setLatestNews(News news) {
        latestNews = news;
        notifyDataSetChanged();
    }

    // Add more data to the RecyclerView
    public void addMoreData(List<News> newData) {
        int start = newsList.size();
        newsList.addAll(newData);
        notifyItemRangeInserted(start, newData.size());
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the type of view based on the position
        if (position == 0 && latestNews != null) {
            return TYPE_CARD; // CardView for the latest news
        } else {
            return TYPE_LIST; // RecyclerView for other news
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_CARD) {
            View cardView = inflater.inflate(R.layout.item_latest_news, parent, false);
            return new CardViewHolder(cardView);
        } else {
            View listView = inflater.inflate(R.layout.item_news, parent, false);
            return new ListViewHolder(listView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CARD) {
            // Bind data for the latest news (CardView)
            CardViewHolder cardHolder = (CardViewHolder) holder;
            cardHolder.bind(latestNews);
        } else {
            // Bind data for other news items (RecyclerView)
            ListViewHolder listHolder = (ListViewHolder) holder;
            listHolder.bind(newsList.get(position - 1)); // Subtract 1 to account for the latest news
        }
    }

    @Override
    public int getItemCount() {
        return latestNews != null ? newsList.size() + 1 : newsList.size();
    }

    // ViewHolder for the latest news (CardView)
    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.latestNewsTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(latestNews);
                }
            });
        }

        void bind(News news) {
            titleTextView.setText(news.getTitle());
            // Bind other views for the latest news here
        }
    }

    // ViewHolder for other news items (RecyclerView)
    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(newsList.get(getAdapterPosition() - 1));
                }
            });
        }

        void bind(News news) {
            titleTextView.setText(news.getTitle());
            // Bind other views for other news items here
        }
    }
}
