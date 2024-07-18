package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.model.YoutubeVideo;

import java.util.Objects;

public class AddYoutubeActivity extends AppCompatActivity {

    private Context context;

    private Button btnAdd;
    private Button btnCancel;

    private EditText etTitre;
    private EditText etDescription;
    private EditText etUrl;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_youtube);

        context = getApplicationContext();
        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAddYoutubeVideo);
        etTitre = findViewById(R.id.etTitre);
        etDescription = findViewById(R.id.etDescription);
        etUrl = findViewById(R.id.etUrl);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        // Retour avec bouton annuler
        btnCancel.setOnClickListener(view -> goMainActivity());

        // ajout avec btnAdd
        btnAdd.setOnClickListener(view -> addYoutubeVideo());

    }

    void addYoutubeVideo(){
        String titreText = etTitre.getText().toString();
        String descriptionText = etDescription.getText().toString();
        String urlText = etUrl.getText().toString();
        String categoryText = spinnerCategory.getSelectedItem().toString();
        if (!titreText.isEmpty() && !descriptionText.isEmpty() && !urlText.isEmpty()) {
            Toast.makeText(context, "Added " + titreText, Toast.LENGTH_SHORT).show();
            etTitre.setText("");
            etDescription.setText("");
            etUrl.setText("");
            YoutubeVideo youtubeVideo = new YoutubeVideo(titreText, descriptionText, urlText, categoryText, false);
            YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().add(youtubeVideo);
            // back to Main
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(context, "Please fill every field", Toast.LENGTH_LONG).show();
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