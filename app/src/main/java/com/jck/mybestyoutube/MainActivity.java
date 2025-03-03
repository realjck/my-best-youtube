package com.jck.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jck.mybestyoutube.database.YoutubeVideoDatabase;
import com.jck.mybestyoutube.pojos.YoutubeVideo;
import com.jck.mybestyoutube.adapters.YoutubeVideoRVAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        YoutubeVideoRVAdapter.OnFavoriteButtonClickListener,
        YoutubeVideoRVAdapter.OnDeleteButtonClickListener {

    private Context context;
    private static final String TAG = "MainActivity";
    private RecyclerView rvYoutubeVideo;
    private YoutubeVideoRVAdapter youtubeVideoRVAdapter;
    private List<YoutubeVideo> youtubeVideos;
    // Préférences de l'application :
    private CheckBox checkboxFavorite;
    private CheckBox checkBoxEdit;
    public static final String CHECKBOX_FAVORITE_KEY = "thumbnail_visibility";
    public static final String CHECKBOX_EDIT_KEY = "edit_visibility";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        rvYoutubeVideo = findViewById(R.id.rvYoutubeVideo);
        rvYoutubeVideo.setLayoutManager(new LinearLayoutManager(this));
        checkboxFavorite = findViewById(R.id.checkBoxFavorite);
        checkBoxEdit = findViewById(R.id.checkBoxEdit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Récupère les vidéos
        youtubeVideos = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().list();
        // log
        for (YoutubeVideo yv : youtubeVideos) {
            Log.d(TAG, yv.getTitle() + " (" + yv.getId() + ")");
        }
        youtubeVideoRVAdapter = new YoutubeVideoRVAdapter(youtubeVideos);
        youtubeVideoRVAdapter.setOnFavoriteButtonClickListener(this);
        youtubeVideoRVAdapter.setOnDeleteButtonClickListener(this);
        // Clic sur un item (View/Edit)
        youtubeVideoRVAdapter.setOnItemClickListener(youtubeVideo -> {
            Intent intent;
            if (checkBoxEdit.isChecked()){
                intent = new Intent(MainActivity.this, EditYoutubeActivity.class);
            } else {
                intent = new Intent(MainActivity.this, ViewYoutubeActivity.class);
            }
            intent.putExtra("videoId", youtubeVideo.getId());
            startActivity(intent);
        });
        rvYoutubeVideo.setAdapter(youtubeVideoRVAdapter);

        // Checkbox de visibilité des ImageButtons Thumbnail et Delete
        checkboxFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            youtubeVideoRVAdapter.setThumbnailVisible(isChecked);
            // Save the checkbox state
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHECKBOX_FAVORITE_KEY, isChecked);
            editor.apply();
        });

        checkBoxEdit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            youtubeVideoRVAdapter.setDeleteVisible(isChecked);
            // Save the checkbox state
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHECKBOX_EDIT_KEY, isChecked);
            editor.apply();
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checkBox1Checked = sharedPreferences.getBoolean(CHECKBOX_FAVORITE_KEY, true);
        boolean checkBox2Checked = sharedPreferences.getBoolean(CHECKBOX_EDIT_KEY, true);
        checkboxFavorite.setChecked(checkBox1Checked);
        checkBoxEdit.setChecked(checkBox2Checked);

    }

    /**
     * Menu items, redirection vers activités
     * @param menu The options menu in which you place your items.
     *
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItemAddYoutube = menu.findItem(R.id.action_add_youtube);
        MenuItem menuItemAddPlaylist = menu.findItem(R.id.action_add_playlist);
        MenuItem menuItemEmptyList = menu.findItem(R.id.action_empty_list);
        menuItemAddYoutube.setOnMenuItemClickListener(item -> {
            // Ajoute Vidéo
            Intent intent = new Intent(context, AddYoutubeActivity.class);
            startActivity(intent);
            return true;
        });
        menuItemAddPlaylist.setOnMenuItemClickListener(item -> {
            // Ajoute Playlist
            Intent intent = new Intent(context, AddPlaylistActivity.class);
            startActivity(intent);
            return true;
        });
        menuItemEmptyList.setOnMenuItemClickListener(item -> {
            // Vide la liste
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.empty_list_confirmation)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        List<YoutubeVideo> youtubeVideos = YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().list();
                        for (YoutubeVideo youtubeVideo : youtubeVideos) {
                            YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().delete(youtubeVideo);
                        }
                        Toast.makeText(context, getString(R.string.empty_list_success), Toast.LENGTH_LONG).show();
                        recreate();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return true;
        });
        return true;
    }

    /**
     * Clic sur un bouton favori d'un élément
     * @param youtubeVideo élement de la RecyclerView
     */
    public void onFavoriteButtonClick(YoutubeVideo youtubeVideo) {
        // Toggle favorite and update database
        youtubeVideo.setFavorite(!youtubeVideo.getFavorite());
        YoutubeVideoDatabase.getDb(context).youtubeVideoDAO().update(youtubeVideo);
        if(youtubeVideo.getFavorite()){
            Toast.makeText(context, youtubeVideo.getTitle() + " " + getString(R.string.favorite_add), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, youtubeVideo.getTitle() + " " + getString(R.string.favorite_remove), Toast.LENGTH_SHORT).show();
        }
        // Update favorite button image
        int position = youtubeVideos.indexOf(youtubeVideo);
        if (position != -1) {
            youtubeVideoRVAdapter.notifyItemChanged(position);
        }
    }

    /**
     * Clic sur un bouton Delete d'un élement
     * @param youtubeVideo élément de la RecyclerView
     */
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
                    Toast.makeText(context, youtubeVideo.getTitle() + " " + getString(R.string.delete_verb), Toast.LENGTH_LONG).show();
                })
                .setNegativeButton(R.string.no, null)
                .show();

    }



}