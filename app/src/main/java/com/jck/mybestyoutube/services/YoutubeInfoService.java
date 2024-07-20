package com.jck.mybestyoutube.services;

import com.jck.mybestyoutube.pojos.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeInfoService {
    @GET("videos")
    Call<VideoResponse> getVideoInfo(@Query("key") String apiKey, @Query("part") String part, @Query("id") String videoId);
}
