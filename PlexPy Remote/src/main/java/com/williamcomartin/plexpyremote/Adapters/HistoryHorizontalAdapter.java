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
import com.williamcomartin.plexpyremote.Models.HistoryModels;
import com.williamcomartin.plexpyremote.R;

import java.util.List;

/**
 * Created by wcomartin on 2015-12-03.
 */
@SuppressWarnings("DefaultFileTemplate")
public class HistoryHorizontalAdapter extends RecyclerView.Adapter<HistoryHorizontalAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView vTitle;
        private final TextView vDate;
        private final NetworkImageView vImage;
        private final TextView vUser;

        public ViewHolder(View itemView) {
            super(itemView);

            vTitle = itemView.findViewById(R.id.history_card_title);
            vDate = itemView.findViewById(R.id.history_card_date);
            vImage = itemView.findViewById(R.id.history_card_image);
            vUser = itemView.findViewById(R.id.history_card_user);

        }
    }

    private List<HistoryModels.HistoryRecord> historyItems;

    // Pass in the contact array into the constructor
    public HistoryHorizontalAdapter(List<HistoryModels.HistoryRecord> historyItems) {
        this.historyItems = historyItems;
    }

    @Override
    public HistoryHorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_history_horizontal, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(HistoryHorizontalAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        HistoryModels.HistoryRecord historyItem = historyItems.get(position);

        // Set item views based on the data model
//        viewHolder.vTitle.setText(historyItem.fullTitle);
//        viewHolder.vUser.setText(historyItem.friendlyName);
        viewHolder.vImage.setImageUrl(UrlHelpers.getImageUrl(historyItem.thumb, "600", "400"),
                ImageCacheManager.getInstance().getImageLoader());
//
//        if(historyItem.date != null && !historyItem.date.equals("null")){
//            SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm a");
////            String date = DateUtils.formatDateTime(ApplicationController.getInstance().getBaseContext(), historyItem.date, 0);
//            String date = format.format(historyItem.date * 1000);
//            viewHolder.vDate.setText(date);
//        }


    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
