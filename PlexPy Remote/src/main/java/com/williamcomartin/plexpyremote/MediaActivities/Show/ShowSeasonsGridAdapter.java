package com.williamcomartin.plexpyremote.MediaActivities.Show;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.MediaActivities.Season.SeasonActivity;
import com.williamcomartin.plexpyremote.Models.LibraryMediaModels;
import com.williamcomartin.plexpyremote.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wcomartin on 2016-12-15.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ShowSeasonsGridAdapter extends BaseAdapter {
    private Context context;
    private List<LibraryMediaModels.LibraryMediaItem> seasons;
    private String parentTitle;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView vCard;
        RelativeLayout vLayout;
        protected NetworkImageView vImage;
        protected TextView vTitle;

        public ViewHolder(final View itemView) {
            super(itemView);
            vCard = itemView.findViewById(R.id.show_seasons_card);
            vLayout = itemView.findViewById(R.id.show_seasons_card_layout);
            vImage = itemView.findViewById(R.id.show_seasons_image);
            vTitle = itemView.findViewById(R.id.show_seasons_title);
        }
    }

    ShowSeasonsGridAdapter(Context context, List<LibraryMediaModels.LibraryMediaItem> seasons) {
        this.context = context;
        setSeasons(seasons);
    }

    public void setParentTitle(String parentTitle){
        this.parentTitle = parentTitle;
    }

    void setSeasons(final List<LibraryMediaModels.LibraryMediaItem> seasons) {
        if(seasons == null) return;
        Collections.sort(seasons, new Comparator<LibraryMediaModels.LibraryMediaItem>() {
            public int compare(LibraryMediaModels.LibraryMediaItem v1, LibraryMediaModels.LibraryMediaItem v2) {
                int v1MediaIndex = Integer.parseInt(v1.mediaIndex);
                int v2MediaIndex = Integer.parseInt(v2.mediaIndex);
                return v1MediaIndex - v2MediaIndex;
            }
        });
        this.seasons = seasons;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final LibraryMediaModels.LibraryMediaItem season = seasons.get(position);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;
        final ShowSeasonsGridAdapter.ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_season, null);
            viewHolder = new ShowSeasonsGridAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ShowSeasonsGridAdapter.ViewHolder) view.getTag();
        }

        viewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, SeasonActivity.class);
                intent.putExtra("RatingKey", season.ratingKey);
                intent.putExtra("Title", parentTitle + " - " + season.title);

                context.startActivity(intent);
            }
        });

        viewHolder.vImage.setImageUrl(UrlHelpers.getImageUrl(season.thumb, "666", "1000"),
                ImageCacheManager.getInstance().getImageLoader());

        viewHolder.vTitle.setText(season.title);

        return view;
    }

    @Override
    public int getCount() {
        return seasons.size();
    }

    @Override
    public LibraryMediaModels.LibraryMediaItem getItem(int position) {
        return seasons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}