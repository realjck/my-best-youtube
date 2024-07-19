package com.jck.mybestyoutube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jck.mybestyoutube.R;
import com.jck.mybestyoutube.models.YoutubeVideo;

import java.util.List;

public class YoutubeVideoRVAdapter extends RecyclerView.Adapter<YoutubeVideoRVAdapter.YoutubeVideoViewHolder> {

    private final List<YoutubeVideo> youtubeVideos;
    private OnFavoriteButtonClickListener mOnFavoriteButtonClickListener;
    public YoutubeVideoRVAdapter(List<YoutubeVideo> youtubeVideos) {
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
        holder.tvTitre.setText(currentYoutubeVideo.getTitle());
        holder.tvDescription.setText(currentYoutubeVideo.getDescription());

        // Set favorite button image
        if (currentYoutubeVideo.getFavorite()) {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_black_on);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_black_off);
        }

        holder.btnFavorite.setOnClickListener(v -> {
            if (mOnFavoriteButtonClickListener != null) {
                mOnFavoriteButtonClickListener.onFavoriteButtonClick(currentYoutubeVideo);
            }
        });
    }


    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public interface OnFavoriteButtonClickListener {
        void onFavoriteButtonClick(YoutubeVideo youtubeVideo);
    }

    public void setOnFavoriteButtonClickListener(OnFavoriteButtonClickListener listener) {
        mOnFavoriteButtonClickListener = listener;
    }

    public static class YoutubeVideoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitre;
        public TextView tvDescription;
        public ImageButton btnFavorite;

        public YoutubeVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }

}
