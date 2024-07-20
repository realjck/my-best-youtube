package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.pojos.YoutubeVideo;

public class ViewYoutubeActivity extends AppCompatActivity {

    private Context context;
    private ImageView ivThumbnail;
    private TextView tvTitle;
    private TextView tvCategory;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_youtube);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = getApplicationContext();
        ivThumbnail = findViewById(R.id.ivThumbnail);
        tvTitle = findViewById(R.id.tvTitle);
        tvCategory = findViewById(R.id.tvCategory);
        tvDescription = findViewById(R.id.tvDescription);

        Long videoId = getIntent().getLongExtra("videoId", -1);
        YoutubeVideo youtubeVideo = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().find(videoId);
        tvTitle.setText(youtubeVideo.getTitle());
        tvCategory.setText(youtubeVideo.getCategory());
        tvDescription.setText(youtubeVideo.getDescription());

        // Build the thumbnail URL
        String thumbnailUrl = "https://img.youtube.com/vi/" + youtubeVideo.getYoutube_id() + "/mqdefault.jpg";

        // Load the thumbnail using Glide
        Glide.with(context)
                .load(thumbnailUrl)
                .fitCenter()
                .into(ivThumbnail);

        // Retour avec flèche toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goMainActivity();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

}