package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.RecentlyAddedModels;
import com.williamcomartin.plexpyremote.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcomartin on 2015-12-03.
 */
@SuppressWarnings("DefaultFileTemplate")
public class RecentlyAddedAdapter extends RecyclerView.Adapter<RecentlyAddedAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView vImage;
        private final TextView vParentTitle;
        private final TextView vTitle;
        private final TextView vDate;

        public ViewHolder(View itemView) {
            super(itemView);

            vImage = itemView.findViewById(R.id.recently_added_card_image);
            vParentTitle = itemView.findViewById(R.id.recently_added_card_parent_title);
            vTitle = itemView.findViewById(R.id.recently_added_card_title);
            vDate = itemView.findViewById(R.id.recently_added_card_date);
        }
    }

    private List<RecentlyAddedModels.RecentItem> recentlyAddedItems = new ArrayList<>();

    // Pass in the contact array into the constructor
    public RecentlyAddedAdapter(List<RecentlyAddedModels.RecentItem> recentlyAddedItems) {
    }

    public void setRecentlyAdded(List<RecentlyAddedModels.RecentItem> recentlyAddedItems) {
        this.recentlyAddedItems.clear();
        if (recentlyAddedItems != null) {
            this.recentlyAddedItems.addAll(recentlyAddedItems);
        }
        notifyDataSetChanged();
    }

    public void addItems(List<RecentlyAddedModels.RecentItem> recentlyAddedItems) {
        if (recentlyAddedItems == null) return;
        this.recentlyAddedItems.addAll(recentlyAddedItems);
        notifyDataSetChanged();
    }

    @Override
    public RecentlyAddedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_recently_added, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(RecentlyAddedAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        RecentlyAddedModels.RecentItem recentlyAddedItem = recentlyAddedItems.get(position);

        if (recentlyAddedItem.parentTitle == "") {
            viewHolder.vTitle.setText(recentlyAddedItem.year);
            viewHolder.vParentTitle.setText(recentlyAddedItem.title);
        } else {
            viewHolder.vTitle.setText(recentlyAddedItem.title);
            viewHolder.vParentTitle.setText(recentlyAddedItem.parentTitle);
        }

        if (recentlyAddedItem.addedAt != null && !recentlyAddedItem.addedAt.equals("null")) {
            Long dateTimeStamp = Long.parseLong(recentlyAddedItem.addedAt) * 1000;
            SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm a");
            String date = format.format(dateTimeStamp);
            viewHolder.vDate.setText(date);
        }

        viewHolder.vImage.setImageUrl(UrlHelpers.getImageUrl(getImageUrl(recentlyAddedItem), "600", "400"),
                ImageCacheManager.getInstance().getImageLoader());
    }

    private String getImageUrl(RecentlyAddedModels.RecentItem recentlyAddedItem) {
        switch (recentlyAddedItem.mediaType) {
            case "episode":
                return recentlyAddedItem.grandparentThumb;
            default:
                return recentlyAddedItem.thumb;
        }
    }

    @Override
    public int getItemCount() {
        if (recentlyAddedItems == null) return 0;
        return recentlyAddedItems.size();
    }
}
