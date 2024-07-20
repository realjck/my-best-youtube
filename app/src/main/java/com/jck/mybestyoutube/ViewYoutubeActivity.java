package com.jck.mybestyoutube;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    private YoutubeVideo youtubeVideo;
    private ImageView ivThumbnail;
    private TextView tvTitle;
    private TextView tvCategory;
    private TextView tvDescription;
    private Button btnLaunchVideo;

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
        btnLaunchVideo = findViewById(R.id.btnLaunchVideo);

        Long videoId = getIntent().getLongExtra("videoId", -1);
        youtubeVideo = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().find(videoId);
        tvTitle.setText(youtubeVideo.getTitle());
        tvCategory.setText(youtubeVideo.getCategory());
        tvDescription.setText(youtubeVideo.getDescription());

        // Launch Youtube video
        btnLaunchVideo.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeVideo.getYoutube_id()));
            try {
                startActivity(intent);
            } catch(ActivityNotFoundException e) {
                Toast.makeText(this, "YouTube app not installed", Toast.LENGTH_SHORT).show();
            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_youtube, menu);
        MenuItem menuItemEdit = menu.findItem(R.id.action_edit);
        MenuItem menuItemDelete = menu.findItem(R.id.action_delete);
        // Go activity Edit
        menuItemEdit.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(context, EditYoutubeActivity.class);
            intent.putExtra("videoId", youtubeVideo.getId());
            startActivity(intent);
            return true;
        });

        menuItemDelete.setOnMenuItemClickListener(item -> {
            YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().delete(youtubeVideo);
            Toast.makeText(context, youtubeVideo.getTitle() + " " + getString(R.string.delete_verb), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            return true;
        });

        return true;
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