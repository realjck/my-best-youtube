package com.jck.mybestyoutube.services;

import com.jck.mybestyoutube.pojos.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeInfoService {

    @GET("videos")
    Call<Response> getVideoInfo(@Query("key") String apiKey, @Query("part") String part, @Query("id") String videoId);

    @GET("playlistItems")
    Call<Response> getPlaylistItemsInfo(
            @Query("key") String apiKey,
            @Query("part") String part,
            @Query("playlistId") String playlistId,
            @Query("maxResults") String maxResults);
}
