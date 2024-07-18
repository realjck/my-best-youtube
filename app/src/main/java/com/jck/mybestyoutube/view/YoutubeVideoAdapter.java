package com.jck.mybestyoutube.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jck.mybestyoutube.R;
import com.jck.mybestyoutube.model.YoutubeVideo;

import java.util.List;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YoutubeVideoViewHolder> {

    private List<YoutubeVideo> youtubeVideos;

    public YoutubeVideoAdapter(List<YoutubeVideo> youtubeVideos) {
        this.youtubeVideos = youtubeVideos;
    }

    @NonNull
    @Override
    public YoutubeVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube_video, parent, false);
        return new YoutubeVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeVideoViewHolder holder, int position) {
        YoutubeVideo currentYoutubeVideo = youtubeVideos.get(position);
        holder.tvTitre.setText(currentYoutubeVideo.getTitre());
        holder.tvDescription.setText(currentYoutubeVideo.getDescription());
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public static class YoutubeVideoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitre;
        public TextView tvDescription;

        public YoutubeVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
