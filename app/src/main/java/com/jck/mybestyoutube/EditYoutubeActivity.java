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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.pojos.YoutubeVideo;

import java.util.Arrays;

public class EditYoutubeActivity extends AppCompatActivity {

    private Context context;
    private EditText etTitle;
    private EditText etDescription;
    private Spinner spinnerCategory;
    private Button btnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_youtube);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = getApplicationContext();
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Retour avec flèche toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Peuple Spinner
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Récupère infos vidéo
        Long videoId = getIntent().getLongExtra("videoId", -1);
        YoutubeVideo youtubeVideo = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().find(videoId);
        etTitle.setText(youtubeVideo.getTitle());
        etDescription.setText(youtubeVideo.getDescription());
        int index = Arrays.asList(categories).indexOf(youtubeVideo.getCategory());
        if (index != -1) {
            spinnerCategory.setSelection(index);
        }

        // Mets à jour au clic
        btnUpdate.setOnClickListener(view -> {
            youtubeVideo.setTitle(etTitle.getText().toString());
            youtubeVideo.setDescription(etDescription.getText().toString());
            youtubeVideo.setCategory(spinnerCategory.getSelectedItem().toString());
            YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().update(youtubeVideo);
            Toast.makeText(context, youtubeVideo.getTitle() + " " + getString(R.string.video_updated), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        });
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