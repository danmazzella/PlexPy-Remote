package com.williamcomartin.plexpyremote.MediaActivities.Season;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.williamcomartin.plexpyremote.Helpers.Exceptions.NoServerException;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.GsonRequest;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.RequestManager;
import com.williamcomartin.plexpyremote.Models.LibraryMediaModels;
import com.williamcomartin.plexpyremote.NavBaseActivity;
import com.williamcomartin.plexpyremote.R;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by wcomartin on 2016-12-14.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SeasonEpisodesFragment extends Fragment {
    private String ratingKey;

    private SeasonEpisodesGridAdapter gridAdapter;
    private String parentTitle;

    public SeasonEpisodesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season_episodes, container, false);

        gridAdapter = new SeasonEpisodesGridAdapter(this.getContext(), new ArrayList<LibraryMediaModels.LibraryMediaItem>());

        GridView vEpisodesGrid = view.findViewById(R.id.show_episodes_grid);
        vEpisodesGrid.setAdapter(gridAdapter);

        fetchProfile();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ActionBar actionBar = ((NavBaseActivity) context).getSupportActionBar();
        if (actionBar != null) {
            parentTitle = String.valueOf(actionBar.getTitle());
        }
    }

    public void setRatingKey(String ratingKey) {
        this.ratingKey = ratingKey;
    }

    private void fetchProfile(){
        String url = "";
        try {
            url = UrlHelpers.getHostPlusAPIKey() + "&cmd=get_library_media_info&length=1000&rating_key=" + ratingKey;
        } catch (NoServerException | MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("ShowSeasonsFrag", url);
        GsonRequest<LibraryMediaModels> request = new GsonRequest<>(
                url,
                LibraryMediaModels.class,
                null,
                requestListener(),
                errorListener()
        );

        RequestManager.addToRequestQueue(request);
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ShowSeasonFrag", error.getMessage());
            }
        };
    }

    private Response.Listener<LibraryMediaModels> requestListener() {
        return new Response.Listener<LibraryMediaModels>() {
            @Override
            public void onResponse(LibraryMediaModels response) {
                Log.d("ShowSeasonFrag", response.toString());
                gridAdapter.setEpisodes(response.response.data.data);
                gridAdapter.setParentTitle(parentTitle);
            }
        };
    }
}
