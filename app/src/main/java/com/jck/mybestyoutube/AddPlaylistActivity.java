package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.pojos.Item;
import com.jck.mybestyoutube.pojos.Snippet;
import com.jck.mybestyoutube.pojos.Response;
import com.jck.mybestyoutube.pojos.YoutubeVideo;
import com.jck.mybestyoutube.services.YoutubeInfoService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPlaylistActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG = "AddPlaylistActivity";
    private EditText etPlaylist;
    private Button btnAddPlaylist;
    private Spinner spinnerCategory;
    private YoutubeInfoService youtubeInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = getApplicationContext();
        etPlaylist = findViewById(R.id.etPlaylist);
        btnAddPlaylist = findViewById(R.id.btnAddPlaylist);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        youtubeInfoService = retrofit.create(YoutubeInfoService.class);

        // Peuple Spinner
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Retour avec flèche toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Listener clic bouton Ajouter
        btnAddPlaylist.setOnClickListener(view -> addPlaylist());

    }


    void addPlaylist(){
        String playlistIdText = etPlaylist.getText().toString();
        if (!playlistIdText.isEmpty()) {
            // Parse Playlist ID
            String youtubeId = "";
            String regexPlaylist = "(?:list=([\\w-]{11,50}))|([\\w-]{11,50})";
            Pattern pattern = Pattern.compile(regexPlaylist);
            Matcher matcher = pattern.matcher(playlistIdText);
            if (matcher.find()) {
                youtubeId = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);

                Call<Response> call = youtubeInfoService.getPlaylistItemsInfo(
                        BuildConfig.API_KEY,
                        "snippet",
                        youtubeId,
                        "50"
                );

                call.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                        if (response.isSuccessful()) {
                            Response videoResponse = response.body();
                            if (videoResponse != null && !videoResponse.getItems().isEmpty()) {
                                List<Item> items = videoResponse.getItems();
                                for (Item item : items) {
                                    Snippet snippet = item.getSnippet();
                                    // créé instances
                                    YoutubeVideo youtubeVideo = new YoutubeVideo(
                                            snippet.getTitle(),
                                            snippet.getDescription(),
                                            snippet.getResourceId().getVideoId(),
                                            spinnerCategory.getSelectedItem().toString(),
                                            false
                                    );
                                    YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().add(youtubeVideo);
                                }
                                // back to Main
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(context, R.string.no_video_found, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(context, R.string.no_youtube_id_found, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, R.string.no_youtube_id_found, Toast.LENGTH_LONG).show();
        }
    }

    // Retour flèche toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}