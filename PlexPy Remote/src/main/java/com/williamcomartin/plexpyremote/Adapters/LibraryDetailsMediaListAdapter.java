package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.williamcomartin.plexpyremote.ApplicationController;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.MediaActivities.Movie.MovieActivity;
import com.williamcomartin.plexpyremote.MediaActivities.Show.ShowActivity;
import com.williamcomartin.plexpyremote.Models.LibraryMediaModels;
import com.williamcomartin.plexpyremote.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wcomartin on 2015-12-03.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LibraryDetailsMediaListAdapter extends RecyclerView.Adapter<LibraryDetailsMediaListAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mCard;
        final NetworkImageView mImage;
        final TextView mTitle;
        final TextView mYear;
        final TextView mLastPlayed;
        final TextView mTotalPlays;
        final TextView mFileSize;

        public ViewHolder(View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.library_details_media_card);
            mImage = itemView.findViewById(R.id.library_details_media_image);
            mTitle = itemView.findViewById(R.id.library_details_media_title);
            mYear = itemView.findViewById(R.id.library_details_media_year);
            mLastPlayed = itemView.findViewById(R.id.library_details_media_last_played);
            mTotalPlays = itemView.findViewById(R.id.library_details_media_total_plays);
            mFileSize = itemView.findViewById(R.id.library_details_media_file_size);
        }
    }

    private List<LibraryMediaModels.LibraryMediaItem> mediaItems = new ArrayList<>();

    // Pass in the contact array into the constructor
    public LibraryDetailsMediaListAdapter(Context context) {
        this.context = context;
        resetItems();
    }

    private void resetItems() {
        this.mediaItems = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItems(List<LibraryMediaModels.LibraryMediaItem> mediaItems) {
        if (mediaItems == null) return;
        Collections.sort(mediaItems, new Comparator<LibraryMediaModels.LibraryMediaItem>() {
            public int compare(LibraryMediaModels.LibraryMediaItem v1, LibraryMediaModels.LibraryMediaItem v2) {
                return getTitle(v1.title).compareTo(getTitle(v2.title));
            }
        });
        this.mediaItems.addAll(mediaItems);
        notifyDataSetChanged();
    }

    @Override
    public LibraryDetailsMediaListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_media_list, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        LibraryMediaModels.LibraryMediaItem mediaItem = mediaItems.get(position);
        return getTitle(mediaItem.title).charAt(0) + "";
    }

    @Override
    public void onBindViewHolder(LibraryDetailsMediaListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final LibraryMediaModels.LibraryMediaItem mediaItem = mediaItems.get(position);

        viewHolder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(mediaItem.mediaType.equals("show")) {
                    intent = new Intent(context, ShowActivity.class);
                    intent.putExtra("RatingKey", mediaItem.ratingKey);
                    intent.putExtra("Title", mediaItem.title);
                }
                if(mediaItem.mediaType.equals("movie")) {
                    intent = new Intent(context, MovieActivity.class);
                    intent.putExtra("RatingKey", mediaItem.ratingKey);
                    intent.putExtra("Title", mediaItem.title);
                }
                if(intent != null) {
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.mImage.setImageUrl(UrlHelpers.getImageUrl(mediaItem.thumb, "400", "600"),
                ImageCacheManager.getInstance().getImageLoader());

        viewHolder.mTitle.setText(mediaItem.title);
        viewHolder.mYear.setText(mediaItem.year);

        if (mediaItem.lastPlayed != null && !mediaItem.lastPlayed.equals("null")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(mediaItem.lastPlayed * 1000);
            viewHolder.mLastPlayed.setText(date);
        } else {
            viewHolder.mLastPlayed.setText("Never");
        }

        if(mediaItem.playCount != null) {
            viewHolder.mTotalPlays.setText(String.valueOf(mediaItem.playCount));
        } else {
            viewHolder.mTotalPlays.setText("None");
        }

        if(mediaItem.fileSize != null) {
            viewHolder.mFileSize.setText(humanReadableByteCount(mediaItem.fileSize, false));
        }

    }

    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private String getTitle(String input) {
        if (input.startsWith("The ")) return input.substring(4);
        return input;
    }

    @Override
    public int getItemCount() {
        if (mediaItems == null) return 0;
        return mediaItems.size();
    }
}
