package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.joanzapata.iconify.widget.IconTextView;
import com.williamcomartin.plexpyremote.ActivityActivity;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.ActivityModels.Activity;
import com.williamcomartin.plexpyremote.R;
import com.williamcomartin.plexpyremote.StreamInfoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by wcomartin on 2015-11-25.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private ActivityActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        IconTextView vInfoIcon;

        protected TextView vTitle;
        TextView vSubTitle;
        TextView vEpisode;

        TextView vStateIcon;
        TextView vUserText;
        TextView vDecisionText;
        TextView vProgressText;

        ProgressBar vprogress;

        NetworkImageView vBackgroundImage;
        protected NetworkImageView vImage;
        NetworkImageView vImageBlurred;

        public ViewHolder(final View itemView) {
            super(itemView);

            vInfoIcon = itemView.findViewById(R.id.activity_card_info_icon);


            vTitle = itemView.findViewById(R.id.activity_card_title);
            vSubTitle = itemView.findViewById(R.id.activity_card_subtitle);
            vEpisode = itemView.findViewById(R.id.activity_card_muted_title);

            vStateIcon = itemView.findViewById(R.id.activity_card_state_icon);
            vUserText = itemView.findViewById(R.id.activity_card_user_text);
            vDecisionText = itemView.findViewById(R.id.activity_card_decision_text);
            vProgressText = itemView.findViewById(R.id.activity_card_progress_text);

            vprogress = itemView.findViewById(R.id.progressbar);

            vBackgroundImage = itemView.findViewById(R.id.activity_card_background_art);
            vImage = itemView.findViewById(R.id.activity_card_image);
            vImageBlurred = itemView.findViewById(R.id.activity_card_image_blurred);

        }
    }

    private List<Activity> activities;

    public ActivityAdapter(Context context, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.activities = new ArrayList<>();
    }

    // Pass in the contact array into the constructor
    public ActivityAdapter(List<Activity> activities) {
        this.activities = activities;
    }

    public void SetActivities(List<Activity> activities) {
        this.activities.clear();
        if (activities != null) {
            this.activities.addAll(activities);
        }
        notifyDataSetChanged();
    }

    public void setActivityView(ActivityActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item_activity, parent, false);

        // Return a new holder instance
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
//        Context context = ApplicationController.getInstance().getApplicationContext();
        // Get the data model based on position
        final Activity activity = activities.get(position);
        final DrawerLayout drawerLayout = mActivity.findViewById(R.id.activity_drawer);
        final StreamInfoFragment frag = (StreamInfoFragment) fragmentManager.findFragmentById(R.id.activity_stream_info_fragment);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)
                && frag.getSessionKey() == Integer.parseInt(activity.session_key)) {
            frag.setStreamInfo(activity);
        }

        viewHolder.vInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
                frag.setStreamInfo(activity);
            }
        });

        // Set item views based on the data model
        String imageUrl;

        switch (activity.media_type) {
            case "episode":
                viewHolder.vTitle.setText(activity.grandparent_title);
                viewHolder.vSubTitle.setText(activity.title);
                String episodeString = "S" + activity.parent_media_index + " • E" + activity.media_index;
                viewHolder.vEpisode.setText(episodeString);
                imageUrl = UrlHelpers.getImageUrl(activity.grandparent_thumb, "600", "400");
                viewHolder.vImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case "track":
                viewHolder.vTitle.setText(activity.grandparent_title);
                viewHolder.vSubTitle.setText(activity.title);
                viewHolder.vEpisode.setText(activity.parent_title);
                imageUrl = UrlHelpers.getImageUrl(activity.thumb, "600", "400");
                break;
            default:
                viewHolder.vTitle.setText(activity.title);
                viewHolder.vEpisode.setText(activity.year);
                imageUrl = UrlHelpers.getImageUrl(activity.thumb, "600", "400");
                viewHolder.vImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
        }

        if (viewHolder.vSubTitle.getText().equals("")) {
            viewHolder.vSubTitle.setVisibility(View.GONE);
        }

        switch (activity.state) {
            case "playing":
                viewHolder.vStateIcon.setText("{fa-play}");
                break;
            case "paused":
                viewHolder.vStateIcon.setText("{fa-pause}");
                break;
            case "buffering":
                viewHolder.vStateIcon.setText("{fa-spinner}");
                break;
        }

        viewHolder.vUserText.setText(activity.friendly_name);

        switch (activity.video_decision) {
            case "direct play":
                viewHolder.vDecisionText.setText("Direct Play");
                break;
            case "copy":
                viewHolder.vDecisionText.setText("Direct Stream");
                break;
            default:
                viewHolder.vDecisionText.setText("Transcoding");
                break;
        }

        String progressText = formatSeconds(activity.view_offset) + "/" + formatSeconds(activity.duration);
        viewHolder.vProgressText.setText(progressText);

        viewHolder.vprogress.setProgress(Integer.parseInt(activity.progress_percent));
        viewHolder.vprogress.setSecondaryProgress(Integer.parseInt(activity.transcode_progress));

        viewHolder.vBackgroundImage.setImageUrl(UrlHelpers.getImageUrl(activity.art, "600", "400"), ImageCacheManager.getInstance().getImageLoader());
        viewHolder.vImage.setImageUrl(imageUrl, ImageCacheManager.getInstance().getImageLoader());
        viewHolder.vImageBlurred.setImageUrl(imageUrl, ImageCacheManager.getInstance().getImageLoader());
        viewHolder.vImageBlurred.setAlpha(0.75f);


    }

    private String formatSeconds(String millis) {
        try {
            return String.format(Locale.US, "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(Integer.parseInt(millis)) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(millis)))
            );
        } catch (NumberFormatException e) {
            return "0:00";
        }
    }

    @Override
    public int getItemCount() {
        if (activities == null) return 0;
        return activities.size();
    }
}
