package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.UserModels;
import com.williamcomartin.plexpyremote.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wcomartin on 2015-11-20.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(UserModels.User item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView vCard;

        TextView vUserName;
        TextView vLastSeen;
        protected TextView vTotalPlays;

        protected TextView vIPAddress;
        protected TextView vPlayer;
        protected TextView vPlatform;
        TextView vLastWatched;


        protected NetworkImageView vImage;

        public ViewHolder(View itemView) {
            super(itemView);

            vCard = itemView.findViewById(R.id.user_card_view);

            vUserName = itemView.findViewById(R.id.user_card_name);
            vLastSeen = itemView.findViewById(R.id.user_card_last_seen);
            vLastWatched = itemView.findViewById(R.id.user_card_watched);

            vImage = itemView.findViewById(R.id.user_card_image);
        }
    }

    private List<UserModels.User> users;

    // Pass in the contact array into the constructor
    public UserAdapter(List<UserModels.User> users, OnItemClickListener listener) {
        if(users != null) {
            Collections.sort(users, new Comparator<UserModels.User>() {
                public int compare(UserModels.User v1, UserModels.User v2) {
                    return v1.friendlyName.compareTo(v2.friendlyName);
                }
            });
            this.users = users;
        }
        this.listener = listener;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_user, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final UserModels.User user = users.get(position);

        // Set item views based on the data model

        viewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user);
            }
        });

        viewHolder.vUserName.setText(user.friendlyName);
        if(user.lastPlayed != null) {
            viewHolder.vLastWatched.setText(user.lastPlayed);
        } else {
            viewHolder.vLastWatched.setText("None");
        }

        viewHolder.vImage.setImageUrl(null, ImageCacheManager.getInstance().getImageLoader());
        if(user.userThumb.equals("interfaces/default/images/gravatar-default-80x80.png")){
            viewHolder.vImage.setImageResource(R.drawable.gravatar_default_circle);
        } else {
            Uri.Builder builder =  Uri.parse(user.userThumb).buildUpon();
            builder.appendQueryParameter("s", "100");
            viewHolder.vImage.setImageUrl(builder.toString(), ImageCacheManager.getInstance().getImageLoader());
            Log.d("UserAdapter", builder.toString());
        }

        if(user.lastSeen != null && !user.lastSeen.equals("null")){
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(user.lastSeen * 1000, System.currentTimeMillis(), 0);
            viewHolder.vLastSeen.setText(timeAgo.toString());
        } else {
            viewHolder.vLastSeen.setText("Never");
        }

    }

    @Override
    public int getItemCount() {
        if(users == null) return 0;
        return users.size();
    }
}
