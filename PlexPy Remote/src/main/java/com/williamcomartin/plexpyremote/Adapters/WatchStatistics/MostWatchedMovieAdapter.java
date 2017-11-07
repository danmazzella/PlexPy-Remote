package com.williamcomartin.plexpyremote.Adapters.WatchStatistics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.StatisticsModels;
import com.williamcomartin.plexpyremote.R;

import java.util.List;

/**
 * Created by wcomartin on 16-05-20.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MostWatchedMovieAdapter extends RecyclerView.Adapter<MostWatchedMovieAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView vImage;
        private final TextView vBadge;
        private final TextView vTitle;
        private final TextView vQuantity;
        private final TextView vQuantifier;

        public ViewHolder(View itemView) {
            super(itemView);

            vImage = itemView.findViewById(R.id.standard_statistics_card_image);
            vBadge = itemView.findViewById(R.id.standard_statistics_card_badge);
            vTitle = itemView.findViewById(R.id.standard_statistics_card_title);
            vQuantity = itemView.findViewById(R.id.standard_statistics_card_quantity);
            vQuantifier = itemView.findViewById(R.id.standard_statistics_card_quantifier);
        }
    }

    private List<StatisticsModels.StatisticsRow> statisticsItems;

    // Pass in the contact array into the constructor
    public MostWatchedMovieAdapter(List<StatisticsModels.StatisticsRow> statisticsItems) {
        this.statisticsItems = statisticsItems;
    }

    @Override
    public MostWatchedMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_standard_statistics, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MostWatchedMovieAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        StatisticsModels.StatisticsRow item = statisticsItems.get(position);

        viewHolder.vImage.setImageUrl(UrlHelpers.getImageUrl(item.thumb, "400", "600"),
                ImageCacheManager.getInstance().getImageLoader());

        viewHolder.vBadge.setText(((Integer) (position + 1)).toString());
        viewHolder.vTitle.setText(item.title);
        viewHolder.vQuantity.setText(item.total_plays.toString());

        viewHolder.vQuantifier.setText("plays");
    }

    @Override
    public int getItemCount() {
        return statisticsItems.size();
    }
}
