package com.jck.mybestyoutube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jck.mybestyoutube.R;
import com.jck.mybestyoutube.pojos.YoutubeVideo;

import java.util.List;

public class YoutubeVideoRVAdapter extends RecyclerView.Adapter<YoutubeVideoRVAdapter.YoutubeVideoViewHolder> {

    private final List<YoutubeVideo> youtubeVideos;
    private OnFavoriteButtonClickListener mOnFavoriteButtonClickListener;
    private OnDeleteButtonClickListener mOnDeleteButtonClickListener;
    public YoutubeVideoRVAdapter(List<YoutubeVideo> youtubeVideos) {
        this.youtubeVideos = youtubeVideos;
    }
    private boolean isThumbnailVisible;
    private boolean isDeleteVisible;

    /**
     * On Create View Holder
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public YoutubeVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube_video, parent, false);
        return new YoutubeVideoViewHolder(view);
    }

    /**
     * On Bind View Holder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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

        // Set Click on delete
        holder.btnDelete.setOnClickListener(v -> {
            if (mOnDeleteButtonClickListener != null) {
                mOnDeleteButtonClickListener.onDeleteButtonClick(currentYoutubeVideo);
            }
        });

        // Build the thumbnail URL
        String thumbnailUrl = "https://img.youtube.com/vi/" + currentYoutubeVideo.getYoutube_id() + "/mqdefault.jpg";

        // Load the thumbnail using Glide
        Glide.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .fitCenter()
                .into(holder.ivThumbnail);

        // Visibility of ImageButtons
        holder.cvThumbnailCard.setVisibility(isThumbnailVisible ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(isDeleteVisible ? View.VISIBLE : View.GONE);
    }


    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    /**
     * Listener click Favorite
     */
    public interface OnFavoriteButtonClickListener {
        void onFavoriteButtonClick(YoutubeVideo youtubeVideo);
    }

    public void setOnFavoriteButtonClickListener(OnFavoriteButtonClickListener listener) {
        mOnFavoriteButtonClickListener = listener;
    }

    /**
     * Listener click Delete
     */
    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(YoutubeVideo youtubeVideo);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        mOnDeleteButtonClickListener = listener;
    }


    public void setThumbnailVisible(boolean isVisible) {
        isThumbnailVisible = isVisible;
        notifyDataSetChanged();
    }

    public void setDeleteVisible(boolean isVisible) {
        isDeleteVisible = isVisible;
        notifyDataSetChanged();
    }



    /**
     * View Holder constructor
     */
    public static class YoutubeVideoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitre;
        public TextView tvDescription;
        public ImageButton btnFavorite;
        public ImageButton btnDelete;
        public ImageView ivThumbnail;
        public CardView cvThumbnailCard;

        public YoutubeVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            cvThumbnailCard = itemView.findViewById(R.id.cvThumbnailCard);
        }
    }

}
