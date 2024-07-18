package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.model.YoutubeVideo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;
    private Context context;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        fabAdd = findViewById(R.id.fabSwitchAddTodo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabAdd.setOnClickListener(view -> goAddYoutube());

        List<YoutubeVideo> youtubeVideos = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().list();
        for (YoutubeVideo yv : youtubeVideos) {
            Log.d(TAG, yv.getTitre() + " (" + yv.getDescription() + ")");
        }
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
}