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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.models.YoutubeVideo;

public class AddYoutubeActivity extends AppCompatActivity {

    private Context context;

    private Button btnAdd;
    private Button btnCancel;

    private EditText etTitle;
    private EditText etDescription;
    private EditText etYoutubeId;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_youtube);

        context = getApplicationContext();
        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAddYoutubeVideo);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etYoutubeId = findViewById(R.id.etYoutubeId);
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
        String titreText = etTitle.getText().toString();
        String descriptionText = etDescription.getText().toString();
        String youtubeIdText = etYoutubeId.getText().toString();
        String categoryText = spinnerCategory.getSelectedItem().toString();
        if (!titreText.isEmpty() && !descriptionText.isEmpty() && !youtubeIdText.isEmpty()) {
            /*
            etTitle.setText("");
            etDescription.setText("");
            etYoutubeId.setText("");
             */
            // Parse Youtube ID
            String youtubeId = "";
            String regex = "(?:v=([\\w-]{11,15}))|([\\w-]{11,15})";
            Pattern pattern = Pattern.compile(regex);
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
                Toast.makeText(context, "Can't find a Youtube ID", Toast.LENGTH_LONG).show();
            }
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