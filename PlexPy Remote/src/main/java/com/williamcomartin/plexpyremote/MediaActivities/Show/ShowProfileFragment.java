package com.williamcomartin.plexpyremote.MediaActivities.Show;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.Helpers.Exceptions.NoServerException;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.GsonRequest;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.RequestManager;
import com.williamcomartin.plexpyremote.R;

import java.net.MalformedURLException;

/**
 * Created by wcomartin on 2016-12-14.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ShowProfileFragment extends Fragment {

    private String ratingKey;

    private NetworkImageView vImage;
    private TextView vStudio;
    private TextView vAired;
    private TextView vRuntime;
    private TextView vRated;
    private RatingBar vRating;

    private TextView vDescription;

    private LinearLayout vStarring;
    private LinearLayout vGenres;

    private Context context;

    public ShowProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_profile, container, false);

        context = this.getContext();

        vImage = view.findViewById(R.id.show_profile_image);
        vStudio = view.findViewById(R.id.show_profile_studio);
        vAired = view.findViewById(R.id.show_profile_aired);
        vRuntime = view.findViewById(R.id.show_profile_runtime);
        vRated = view.findViewById(R.id.show_profile_rated);
        vRating = view.findViewById(R.id.show_profile_rating);

        vDescription = view.findViewById(R.id.show_profile_description);

        vStarring = view.findViewById(R.id.show_profile_starring);
        vGenres = view.findViewById(R.id.show_profile_genres);

        fetchProfile();
        return view;
    }

    public void setRatingKey(String ratingKey) {
        this.ratingKey = ratingKey;
    }

    private void fetchProfile(){
        String url = "";
        try {
            url = UrlHelpers.getHostPlusAPIKey() + "&cmd=get_metadata&rating_key=" + ratingKey;
        } catch (NoServerException | MalformedURLException e) {
            e.printStackTrace();
        }
        GsonRequest<ShowProfileModels> request = new GsonRequest<>(
                url,
                ShowProfileModels.class,
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
            }
        };
    }

    private Response.Listener<ShowProfileModels> requestListener() {
        return new Response.Listener<ShowProfileModels>() {
            @Override
            public void onResponse(ShowProfileModels response) {

                ShowProfileModels.Data item;
                if(response.response.data.metadata != null) item = response.response.data.metadata;
                else item = response.response.data;

                vImage.setImageUrl(UrlHelpers.getImageUrl(item.thumb, "400", "600"),
                        ImageCacheManager.getInstance().getImageLoader());

                vStudio.setText(item.studio);
                vAired.setText(item.year);
                int duration;
                try {
                    duration = Integer.parseInt(item.duration) / 60 / 1000;
                } catch (NumberFormatException e){
                    duration = 0;
                }
                String minDuration = String.valueOf(duration) + " mins";
                vRuntime.setText(minDuration);
                vRated.setText(item.contentRating);

                try {
                    vRating.setRating(Float.parseFloat(item.rating) / 2);
                } catch (NumberFormatException e){
                    vRating.setRating(0.0f);
                } catch (NullPointerException e){
                    vRating.setRating(0.0f);
                }

                vDescription.setText(item.summary);

                int listLength = 0;
                if(item.actors != null) {
                    if (item.actors.size() > 5) {
                        listLength = 5;
                    } else {
                        listLength = item.actors.size();
                    }
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                int marginInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                params.setMargins(marginInDp, 0, 0, 0);

                int color = ContextCompat.getColor(context, R.color.colorTextHeading);



                for (int i = 0; i < listLength; i++){
                    TextView text = new TextView(context);
                    text.setLayoutParams(params);

                    SpannableString s = new SpannableString(item.actors.get(i));
                    s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);

                    text.setText(s);
                    vStarring.addView(text);
                }

                if(item.genres != null) {
                    for (String genre : item.genres) {
                        TextView text = new TextView(context);
                        text.setLayoutParams(params);

                        SpannableString s = new SpannableString(genre);
                        s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);

                        text.setText(s);
                        vGenres.addView(text);
                    }
                }
            }
        };
    }
}
