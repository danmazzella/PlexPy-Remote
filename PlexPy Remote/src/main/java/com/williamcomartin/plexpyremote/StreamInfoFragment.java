package com.williamcomartin.plexpyremote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.joanzapata.iconify.widget.IconTextView;
import com.williamcomartin.plexpyremote.Helpers.ImageHelper;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.ActivityModels;
import com.williamcomartin.plexpyremote.Models.ActivityModels.Activity;
import com.williamcomartin.plexpyremote.Services.PlatformService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StreamInfoFragment extends Fragment {

    private NetworkImageView vImage;
    private TextView vEta;
    private TextView vRemaining;
    private IconTextView vState;
    private ProgressBar vProgress;

    private TextView vTitle;
    private TextView vSubtitle;
    private TextView vEpisode;

    private NetworkImageView vUserAvatar;
    private TextView vUserName;
    private TextView vUserIP;

    private ImageView vPlayerAvatar;
    private TextView vPlayerName;
    private TextView vPlayerPlatform;

    private TextView vStreamDecision;
    private TextView vVideoDecision;
    private TextView vAudioDecision;

    private ScrollView vScroller;
    private Activity activity;

    public StreamInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stream_info, container, false);

        vImage = view.findViewById(R.id.activity_stream_thumb);
        vEta = view.findViewById(R.id.activity_stream_eta);
        vRemaining = view.findViewById(R.id.activity_stream_time_remaining);
        vState = view.findViewById(R.id.activity_stream_state_icon);
        vProgress = view.findViewById(R.id.activity_stream_progress_bar);

        vTitle = view.findViewById(R.id.activity_stream_title);
        vSubtitle = view.findViewById(R.id.activity_stream_subtitle);
        vEpisode = view.findViewById(R.id.activity_stream_episode);

        vUserAvatar = view.findViewById(R.id.activity_stream_user_avatar);
        vUserName = view.findViewById(R.id.activity_stream_user_name);
        vUserIP = view.findViewById(R.id.activity_stream_user_ip);

        vPlayerAvatar = view.findViewById(R.id.activity_stream_player_avatar);
        vPlayerName = view.findViewById(R.id.activity_stream_player_name);
        vPlayerPlatform = view.findViewById(R.id.activity_stream_player_platform);

        vStreamDecision = view.findViewById(R.id.activity_stream_stream_decision);
        vVideoDecision = view.findViewById(R.id.activity_stream_video_decision);
        vAudioDecision = view.findViewById(R.id.activity_stream_audio_decision);

        vScroller = view.findViewById(R.id.activity_stream_scroller);

        return view;
    }

    public int getSessionKey() {
        if(activity.session_key == null) return 0;
        return Integer.parseInt(activity.session_key);
    }

    public void setStreamInfo(ActivityModels.Activity activity) {
        this.activity = activity;
        vScroller.fullScroll(ScrollView.FOCUS_UP);

        vImage.setImageUrl(UrlHelpers.getImageUrl(activity.art, "400", "600"),
                ImageCacheManager.getInstance().getImageLoader());

        vEta.setText(String.format(getString(R.string.eta), formatDuration(activity.duration, activity.view_offset)));

        String remainingString = formatSeconds(activity.view_offset) + "/" + formatSeconds(activity.duration);
        vRemaining.setText(remainingString);

        switch (activity.state) {
            case "playing":
                vState.setText("{fa-play}");
                break;
            case "paused":
                vState.setText("{fa-pause}");
                break;
            case "buffering":
                vState.setText("{fa-spinner}");
                break;
        }

        vProgress.setProgress(Integer.parseInt(activity.progress_percent));
        vProgress.setSecondaryProgress(Integer.parseInt(activity.transcode_progress));

        switch (activity.media_type) {
            case "episode":
                vTitle.setText(activity.grandparent_title);
                vSubtitle.setText(activity.title);
                String episodeText = "S" + activity.parent_media_index + " • E" + activity.media_index;
                vEpisode.setText(episodeText);
                break;
            case "track":
                vTitle.setText(activity.grandparent_title);
                vSubtitle.setText(activity.title);
                vEpisode.setText(activity.parent_title);
                break;
            default:
                vTitle.setText(activity.title);
                vSubtitle.setText("");
                vEpisode.setText(activity.year);
                break;
        }

        if (vSubtitle.getText().equals("")) {
            vSubtitle.setVisibility(View.GONE);
        } else {
            vSubtitle.setVisibility(View.VISIBLE);
        }

        vUserAvatar.setImageUrl(activity.user_thumb,
                ImageCacheManager.getInstance().getImageLoader());
        vUserName.setText(activity.friendly_name);
        String ipAddress = "IP: " + activity.ip_address;
        vUserIP.setText(ipAddress);

//            String url = UrlHelpers.getHost() + PlatformService.getInstance().getPlatformImagePath(activity.platform);
        Bitmap platform = BitmapFactory.decodeResource(
                getResources(), PlatformService.getInstance().getPlatformImagePath(activity.platform));
        vPlayerAvatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(platform, 30));
        vPlayerName.setText(activity.player);
        vPlayerPlatform.setText(activity.platform);

        vStreamDecision.setText(buildStreamString(activity));
        vVideoDecision.setText(buildVideoString(activity));
        vAudioDecision.setText(buildAudioString(activity));
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

    private String formatDuration(String duration, String difference){
        int timeleft = Integer.parseInt(duration) - Integer.parseInt(difference);
        long eta = new Date().getTime() + timeleft;
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm", Locale.US);
        return dt.format(new Date(eta));
    }

    private String buildStreamString(Activity activity) {
        String stream = "";

        if(activity.media_type.equals("track")){
            switch (activity.audio_decision) {
                case "direct play":
                    stream += "Direct Play";
                    break;
                case "copy":
                    stream += "Direct Stream";
                    break;
                default:
                    stream += "Transcoding";
                    stream += " (" + activity.transcode_speed + ")";
                    if (activity.throttled.equals("1")) {
                        stream += " (Throttled)";
                    }
                    break;
            }
        } else {
            if(activity.audio_decision.equals("direct play") && activity.video_decision.equals("direct play")){
                stream += "Direct Play";
            } else if (activity.audio_decision.equals("copy") && activity.video_decision.equals("copy")){
                stream += "Direct Stream";
            } else {
                stream += "Transcoding";
                stream += " (" + activity.transcode_speed + ")";
                if(activity.throttled.equals("1")){
                    stream += " (Throttled)";
                }
            }
        }

        return stream;
    }

    private String buildVideoString(Activity activity) {
        String video = "";

        switch (activity.video_decision) {
            case "direct play":
                video += "Direct Play";
                video += " (" + activity.video_codec + ")";
                video += " (" + activity.width + "x" + activity.height + ")";
                break;
            case "copy":
                video += "Direct Stream";
                video += " (" + activity.transcode_video_codec + ")";
                video += " (" + activity.width + "x" + activity.height + ")";
                break;
            default:
                video += "Transcoding";
                video += " (" + activity.transcode_video_codec + ")";
                video += " (" + activity.transcode_width + "x" + activity.transcode_height + ")";
                break;
        }

        return video;
    }

    private String buildAudioString(Activity activity) {
        String audio = "";

        switch (activity.audio_decision) {
            case "direct play":
                audio += "Direct Play";
                audio += " (" + activity.audio_codec + ")";
                audio += " (" + activity.audio_channels + " ch)";
                break;
            case "copy":
                audio += "Direct Stream";
                audio += " (" + activity.transcode_audio_codec + ")";
                audio += " (" + activity.transcode_audio_channels + " ch)";
                break;
            default:
                audio += "Transcoding";
                audio += " (" + activity.transcode_audio_codec + ")";
                audio += " (" + activity.transcode_audio_channels + " ch)";
                break;
        }

        return audio;
    }
}
