package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.LibraryDetailsActivity;
import com.williamcomartin.plexpyremote.Models.LibraryStatisticsModels;
import com.williamcomartin.plexpyremote.R;

import java.util.List;

/**
 * Created by wcomartin on 2015-12-03.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LibraryStatisticsAdapter extends RecyclerView.Adapter<LibraryStatisticsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView vCard;
        private final TextView vTitle;
        private final TextView vCountTitle;
        private final TextView vCount;
        private final TextView vChildCountTitle;
        private final TextView vChildCount;
        private final LinearLayout vEpisodeLayout;
        private final NetworkImageView vImage;

        public ViewHolder(View itemView) {
            super(itemView);

            vCard = itemView.findViewById(R.id.recently_added_card_view);
            vTitle = itemView.findViewById(R.id.library_statistics_card_title);
            vCountTitle = itemView.findViewById(R.id.library_statistics_card_count_title);
            vCount = itemView.findViewById(R.id.library_statistics_card_count);
            vChildCountTitle = itemView.findViewById(R.id.library_statistics_card_child_count_title);
            vChildCount = itemView.findViewById(R.id.library_statistics_card_child_count);
            vImage = itemView.findViewById(R.id.library_statistics_card_image);

            vEpisodeLayout = itemView.findViewById(R.id.library_statistics_card_child);

        }
    }

    private Context context;
    private List<LibraryStatisticsModels.LibraryStat> libraryStatisticsItem;

    // Pass in the contact array into the constructor
    public LibraryStatisticsAdapter(Context context, List<LibraryStatisticsModels.LibraryStat> libraryStatisticsItem) {
        this.context = context;
        this.libraryStatisticsItem = libraryStatisticsItem;
    }

    @Override
    public LibraryStatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_library_statistics, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(LibraryStatisticsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final LibraryStatisticsModels.LibraryStat item = libraryStatisticsItem.get(position);

        viewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LibraryDetailsActivity.class);
                intent.putExtra("LibraryTitle", item.sectionName);
                intent.putExtra("LibraryId", item.sectionId);
                context.startActivity(intent);
            }
        });

        // Set item views based on the data model
        viewHolder.vTitle.setText(item.sectionName);
        viewHolder.vCount.setText(item.count);
        switch (item.sectionType) {
            case "artist":
                viewHolder.vCountTitle.setText("Artists");
                viewHolder.vChildCountTitle.setText("Albums");
                viewHolder.vEpisodeLayout.setVisibility(LinearLayout.VISIBLE);
                viewHolder.vChildCount.setText(item.parentCount);
                break;
            case "movie":
                viewHolder.vCountTitle.setText("Movies");
                viewHolder.vEpisodeLayout.setVisibility(LinearLayout.GONE);
                break;
            case "show":
                viewHolder.vCountTitle.setText("Shows");
                viewHolder.vChildCountTitle.setText("Episodes");
                viewHolder.vEpisodeLayout.setVisibility(LinearLayout.VISIBLE);
                viewHolder.vChildCount.setText(item.childCount);
                break;
        }

        viewHolder.vImage.setImageUrl(UrlHelpers.getImageUrl(item.thumb, "400", "400", "cover"),
                ImageCacheManager.getInstance().getImageLoader());

    }

    @Override
    public int getItemCount() {
        if(libraryStatisticsItem == null) return 0;
        return libraryStatisticsItem.size();
    }
}
