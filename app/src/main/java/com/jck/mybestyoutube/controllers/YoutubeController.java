package com.jck.mybestyoutube.controllers;

import com.jck.mybestyoutube.pojos.Response;
import com.jck.mybestyoutube.services.YoutubeInfoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YoutubeController {
    private final YoutubeInfoService youtubeInfoService;

    public YoutubeController() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        youtubeInfoService = retrofit.create(YoutubeInfoService.class);
    }

    public void getVideoInfo(String apiKey, String part, String videoId, Callback<Response> callback) {
        Call<Response> call = youtubeInfoService.getVideoInfo(apiKey, part, videoId);
        call.enqueue(callback);
    }

    public void getPlaylistItemsInfo(String apiKey, String part, String playlistId, String maxResults, Callback<Response> callback) {
        Call<Response> call = youtubeInfoService.getPlaylistItemsInfo(apiKey, part, playlistId, maxResults);
        call.enqueue(callback);
    }
}
