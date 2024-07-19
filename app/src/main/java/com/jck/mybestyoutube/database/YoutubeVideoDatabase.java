package com.jck.mybestyoutube.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jck.mybestyoutube.pojos.YoutubeVideo;
import com.jck.mybestyoutube.persistence.YoutubeVideoDAO;

@Database(entities = {YoutubeVideo.class}, version = 2)
public abstract class YoutubeVideoDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "my_best_youtube";

    public static YoutubeVideoDatabase getDb(Context context) {
        return Room.databaseBuilder(context, YoutubeVideoDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
    public abstract YoutubeVideoDAO youtubeVideoDAO();
}
