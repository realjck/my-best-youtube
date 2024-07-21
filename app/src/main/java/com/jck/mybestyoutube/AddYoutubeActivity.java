package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.pojos.Snippet;
import com.jck.mybestyoutube.pojos.VideoResponse;
import com.jck.mybestyoutube.pojos.YoutubeVideo;
import com.jck.mybestyoutube.services.YoutubeInfoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddYoutubeActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG = "AddYoutubeActivity";
    private Button btnAdd;
    private Button btnGetVideoInfo;
    private EditText etTitle;
    private EditText etDescription;
    private EditText etYoutubeId;
    private Spinner spinnerCategory;
    private LinearLayout llFormSend;
    private LinearLayout llFormSearch;
    private YoutubeInfoService youtubeInfoService;
    // Regex pour récupérer l'identifiant Youtube d'une URL ou ID seul
    private final String regexYoutube = "(?:v=([\\w-]{11,15}))|([\\w-]{11,15})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_youtube);

        context = getApplicationContext();
        btnAdd = findViewById(R.id.btnAddYoutubeVideo);
        btnGetVideoInfo = findViewById(R.id.btnGetVideoInfo);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etYoutubeId = findViewById(R.id.etYoutubeId);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        llFormSend = findViewById(R.id.llFormSend);
        llFormSearch = findViewById(R.id.llFormSearch);

        // Cache deuxième partie du formulaire
        llFormSend.setVisibility(View.GONE);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        youtubeInfoService = retrofit.create(YoutubeInfoService.class);

        btnGetVideoInfo.setOnClickListener(view -> getVideoInfo());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        btnAdd.setOnClickListener(view -> addYoutubeVideo());

    }

    /**
     * Récupère les infos Titre et Description grâce à l'API Google Youtube
     */
    void getVideoInfo(){
        String youtubeIdText = etYoutubeId.getText().toString();
        if (!youtubeIdText.isEmpty()) {
            // Parse Youtube ID
            String youtubeId = "";
            Pattern pattern = Pattern.compile(regexYoutube);
            Matcher matcher = pattern.matcher(youtubeIdText);
            if (matcher.find()) {
                youtubeId = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);

                Call<VideoResponse> call = youtubeInfoService.getVideoInfo(
                        BuildConfig.API_KEY,
                        "snippet",
                        youtubeId
                );

                call.enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VideoResponse> call, @NonNull Response<VideoResponse> response) {
                        if (response.isSuccessful()) {
                            VideoResponse videoResponse = response.body();
                            if (videoResponse != null && !videoResponse.getItems().isEmpty()) {
                                Snippet snippet = videoResponse.getItems().get(0).getSnippet();
                                Log.d(TAG, snippet.toString());
                                // Set text informations
                                etTitle.setText(snippet.getTitle());
                                etDescription.setText(snippet.getDescription());
                                // Set visibility formulaire
                                llFormSearch.setVisibility(View.GONE);
                                llFormSend.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(context, R.string.no_video_found, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VideoResponse> call, @NonNull Throwable t) {
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

    /**
     * Ajoute la vidéo à la base de données
     */
    void addYoutubeVideo(){
        String titreText = etTitle.getText().toString();
        String descriptionText = etDescription.getText().toString();
        String youtubeIdText = etYoutubeId.getText().toString();
        String categoryText = spinnerCategory.getSelectedItem().toString();
        if (!titreText.isEmpty() && !descriptionText.isEmpty() && !youtubeIdText.isEmpty()) {
            // Parse Youtube ID
            String youtubeId = "";
            Pattern pattern = Pattern.compile(regexYoutube);
            Matcher matcher = pattern.matcher(youtubeIdText);
            if (matcher.find()) {
                youtubeId = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                YoutubeVideo youtubeVideo = new YoutubeVideo(titreText, descriptionText, youtubeId, categoryText, false);
                YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().add(youtubeVideo);
                Toast.makeText(context, "Added " + titreText, Toast.LENGTH_SHORT).show();
                // back to Main
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, R.string.no_youtube_id_found, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_LONG).show();
        }
    }

    // Retour flèche toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goMainActivity();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }
}