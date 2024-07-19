package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.models.YoutubeVideo;
import com.jck.mybestyoutube.adapters.YoutubeVideoRVAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        YoutubeVideoRVAdapter.OnFavoriteButtonClickListener,
        YoutubeVideoRVAdapter.OnDeleteButtonClickListener {

    private FloatingActionButton fabAdd;
    private Context context;
    private static final String TAG = "MainActivity";
    private RecyclerView rvYoutubeVideo;
    private YoutubeVideoRVAdapter youtubeVideoRVAdapter;
    private List<YoutubeVideo> youtubeVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        fabAdd = findViewById(R.id.fabSwitchAddTodo);
        rvYoutubeVideo = findViewById(R.id.rvYoutubeVideo);
        rvYoutubeVideo.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabAdd.setOnClickListener(view -> goAddYoutube());

        youtubeVideos = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().list();
        for (YoutubeVideo yv : youtubeVideos) {
            Log.d(TAG, yv.getTitle() + " (" + yv.getYoutube_id() + ")");
        }
        youtubeVideoRVAdapter = new YoutubeVideoRVAdapter(youtubeVideos);
        youtubeVideoRVAdapter.setOnFavoriteButtonClickListener(this);
        youtubeVideoRVAdapter.setOnDeleteButtonClickListener(this);
        rvYoutubeVideo.setAdapter(youtubeVideoRVAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_settings);
        menuItem.setOnMenuItemClickListener(item -> {
            goAddYoutube();
            return true;
        });
        return true;
    }

    private void goAddYoutube() {
        Intent intent = new Intent(context, AddYoutubeActivity.class);
        startActivity(intent);
    }

    public void onFavoriteButtonClick(YoutubeVideo youtubeVideo) {
        // Toggle favorite and update database
        youtubeVideo.setFavorite(!youtubeVideo.getFavorite());
        YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().update(youtubeVideo);
        // Update favorite button image
        int position = youtubeVideos.indexOf(youtubeVideo);
        if (position != -1) {
            youtubeVideoRVAdapter.notifyItemChanged(position);
        }
    }

    public void onDeleteButtonClick(YoutubeVideo youtubeVideo) {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_content)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().delete(youtubeVideo);
                    int position = youtubeVideos.indexOf(youtubeVideo);
                    if (position != -1) {
                        youtubeVideos.remove(position);
                        youtubeVideoRVAdapter.notifyItemRemoved(position);
                    }
                    Toast.makeText(context, youtubeVideo.getTitle() + R.string.delete_verb, Toast.LENGTH_LONG).show();
                })
                .setNegativeButton(R.string.no, null)
                .show();

    }

}