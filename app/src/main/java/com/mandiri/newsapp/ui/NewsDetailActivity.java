package com.mandiri.newsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.mandiri.newsapp.R;

public class NewsDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESCRIPTION = "extra_description";

    private TextView titleTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        if (getIntent() != null) {
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            String description = getIntent().getStringExtra(EXTRA_DESCRIPTION);

            titleTextView.setText(title);
            descriptionTextView.setText(description);
        }
    }
}
