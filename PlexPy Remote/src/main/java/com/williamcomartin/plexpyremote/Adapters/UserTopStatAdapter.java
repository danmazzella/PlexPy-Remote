package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.LibraryUsersStatsModels;
import com.williamcomartin.plexpyremote.R;

import java.util.List;

/**
 * Created by wcomartin on 2016-11-27.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserTopStatAdapter extends BaseAdapter {
    private Context context;
    private List<LibraryUsersStatsModels.LibraryUsersStat> users;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView vAvatar;
        TextView vName;
        TextView vPlays;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.vAvatar = itemView.findViewById(R.id.user_top_stat_image);
            this.vName = itemView.findViewById(R.id.user_top_stat_name);
            this.vPlays = itemView.findViewById(R.id.user_top_stat_plays);
        }
    }

    public UserTopStatAdapter(Context context, List<LibraryUsersStatsModels.LibraryUsersStat> users) {
        this.context = context;
        this.users = users;
    }

    public void setUsers(List<LibraryUsersStatsModels.LibraryUsersStat> users){
        this.users = users;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final LibraryUsersStatsModels.LibraryUsersStat user = users.get(position);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;
        final ViewHolder viewHolder;

        if(view == null){
            assert inflater != null;
            view = inflater.inflate(R.layout.item_user_top_stat, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.vAvatar.setImageUrl(user.userThumb, ImageCacheManager.getInstance().getImageLoader());
        viewHolder.vAvatar.setDefaultImageResId(R.drawable.gravatar_default_circle);
        viewHolder.vAvatar.setErrorImageResId(R.drawable.gravatar_default_circle);

        viewHolder.vName.setText(user.friendlyName);
        viewHolder.vPlays.setText(String.valueOf(user.totalPlays));



        return view;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public LibraryUsersStatsModels.LibraryUsersStat getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
