package com.williamcomartin.plexpyremote;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.joanzapata.iconify.widget.IconTextView;
import com.williamcomartin.plexpyremote.Helpers.AnimationHelper;
import com.williamcomartin.plexpyremote.Helpers.ImageHelper;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.StatisticsModels;
import com.williamcomartin.plexpyremote.Services.PlatformService;

/**
 * Created by wcomartin on 2017-01-13.
 */
@SuppressWarnings("DefaultFileTemplate")
public class StatisticsFragment extends Fragment {

    private StatisticsModels.StatisticsGroup stats;
    private String type;

    public StatisticsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        TextView vType = view.findViewById(R.id.statistics_primary_card_type);
        vType.setText(this.type);

        if(this.stats != null) {
            if(this.stats.rows != null) {
                try {
                    setTopStat(view, this.stats.rows.get(0));
                } catch (IndexOutOfBoundsException e) {

                }

                try {
                    setStat1(view, this.stats.rows.get(1));
                } catch (IndexOutOfBoundsException e) {

                }

                try {
                    setStat2(view, this.stats.rows.get(2));
                } catch (IndexOutOfBoundsException e) {

                }

                try {
                    setStat3(view, this.stats.rows.get(3));
                } catch (IndexOutOfBoundsException e) {

                }

                try {
                    setStat4(view, this.stats.rows.get(4));
                } catch (IndexOutOfBoundsException e) {

                }
            }
        }

        final CardView vSecondaryCard = view.findViewById(R.id.statistics_secondary_card);

        final IconTextView vChevron = view.findViewById(R.id.statistics_primary_card_chevron);
        vChevron.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(vSecondaryCard.getVisibility() == View.GONE) {
                    vChevron.setText("{fa-chevron-up}");
                    AnimationHelper.expand(vSecondaryCard);
                } else {
                    vChevron.setText("{fa-chevron-down}");
                    AnimationHelper.collapse(vSecondaryCard);
                }
            }
        });

        return view;
    }

    public void setTopStat(View view, StatisticsModels.StatisticsRow stat){
        TextView vTitle = view.findViewById(R.id.statistics_primary_card_title);
        vTitle.setText(stat.title);

        TextView vPlays = view.findViewById(R.id.statistics_primary_card_plays);
        if(this.stats.isTop()){
            vPlays.setText(String.valueOf(stat.total_plays));
        }
        if(this.stats.isPopular()){
            vPlays.setText(String.valueOf(stat.users_watched));
        }

        NetworkImageView vImage = view.findViewById(R.id.statistics_primary_card_image);
        NetworkImageView vCircleImage = view.findViewById(R.id.statistics_primary_card_circle_image);
        ImageView vSquareImage = view.findViewById(R.id.statistics_primary_card_square_image);

        if(this.stats.isTV()){
            vImage.setImageUrl(UrlHelpers.getImageUrl(stat.grandparent_thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isMovie()) {
            vImage.setImageUrl(UrlHelpers.getImageUrl(stat.thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isUser()) {
            vCircleImage.setImageUrl(stat.user_thumb, ImageCacheManager.getInstance().getImageLoader());
            vTitle.setText(stat.friendly_name);
        }

        if(this.stats.isPlatform()) {
            Bitmap platform = BitmapFactory.decodeResource(
                    getResources(), PlatformService.getInstance().getPlatformImagePath(stat.platform));
            vSquareImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(platform, 30));
            vTitle.setText(stat.platform);
        }
    }

    public void setStat1(View view, StatisticsModels.StatisticsRow stat){
        TextView vTitle1 = view.findViewById(R.id.statistics_secondary_card_1_title);
        vTitle1.setText(stat.title);

        TextView vPlays1 = view.findViewById(R.id.statistics_secondary_card_1_plays);
        if(this.stats.isTop()){
            vPlays1.setText(String.valueOf(stat.total_plays));
        }
        if(this.stats.isPopular()){
            vPlays1.setText(String.valueOf(stat.users_watched));
        }

        NetworkImageView vImage1 = view.findViewById(R.id.statistics_secondary_card_1_image);
        NetworkImageView vCircleImage1 = view.findViewById(R.id.statistics_secondary_card_1_circle_image);
        ImageView vSquareImage1 = view.findViewById(R.id.statistics_secondary_card_1_square_image);

        if(this.stats.isTV()){
            vImage1.setImageUrl(UrlHelpers.getImageUrl(stat.grandparent_thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isMovie()) {
            vImage1.setImageUrl(UrlHelpers.getImageUrl(stat.thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isUser()) {
            vCircleImage1.setImageUrl(stat.user_thumb, ImageCacheManager.getInstance().getImageLoader());
            vTitle1.setText(stat.friendly_name);
        }

        if(this.stats.isPlatform()) {
            Bitmap platform = BitmapFactory.decodeResource(
                    getResources(), PlatformService.getInstance().getPlatformImagePath(stat.platform));
            vSquareImage1.setImageBitmap(ImageHelper.getRoundedCornerBitmap(platform, 30));
            vTitle1.setText(stat.platform);
        }
    }

    public void setStat2(View view, StatisticsModels.StatisticsRow stat){
        TextView vTitle2 = view.findViewById(R.id.statistics_secondary_card_2_title);
        vTitle2.setText(stat.title);

        TextView vPlays2 = view.findViewById(R.id.statistics_secondary_card_2_plays);
        if(this.stats.isTop()){
            vPlays2.setText(String.valueOf(stat.total_plays));
        }
        if(this.stats.isPopular()){
            vPlays2.setText(String.valueOf(stat.users_watched));
        }

        NetworkImageView vImage2 = view.findViewById(R.id.statistics_secondary_card_2_image);
        NetworkImageView vCircleImage2 = view.findViewById(R.id.statistics_secondary_card_2_circle_image);
        ImageView vSquareImage2 = view.findViewById(R.id.statistics_secondary_card_2_square_image);

        if(this.stats.isTV()){
            vImage2.setImageUrl(UrlHelpers.getImageUrl(stat.grandparent_thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isMovie()) {
            vImage2.setImageUrl(UrlHelpers.getImageUrl(stat.thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isUser()) {
            vCircleImage2.setImageUrl(stat.user_thumb, ImageCacheManager.getInstance().getImageLoader());
            vTitle2.setText(stat.friendly_name);
        }

        if(this.stats.isPlatform()) {
            Bitmap platform = BitmapFactory.decodeResource(
                    getResources(), PlatformService.getInstance().getPlatformImagePath(stat.platform));
            vSquareImage2.setImageBitmap(ImageHelper.getRoundedCornerBitmap(platform, 30));
            vTitle2.setText(stat.platform);
        }
    }

    public void setStat3(View view, StatisticsModels.StatisticsRow stat){
        TextView vTitle3 = view.findViewById(R.id.statistics_secondary_card_3_title);
        vTitle3.setText(stat.title);

        TextView vPlays3 = view.findViewById(R.id.statistics_secondary_card_3_plays);
        if(this.stats.isTop()){
            vPlays3.setText(String.valueOf(stat.total_plays));
        }
        if(this.stats.isPopular()){
            vPlays3.setText(String.valueOf(stat.users_watched));
        }

        NetworkImageView vImage3 = view.findViewById(R.id.statistics_secondary_card_3_image);
        NetworkImageView vCircleImage3 = view.findViewById(R.id.statistics_secondary_card_3_circle_image);
        ImageView vSquareImage3 = view.findViewById(R.id.statistics_secondary_card_3_square_image);

        if(this.stats.isTV()){
            vImage3.setImageUrl(UrlHelpers.getImageUrl(stat.grandparent_thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isMovie()) {
            vImage3.setImageUrl(UrlHelpers.getImageUrl(stat.thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isUser()) {
            vCircleImage3.setImageUrl(stat.user_thumb, ImageCacheManager.getInstance().getImageLoader());
            vTitle3.setText(stat.friendly_name);
        }

        if(this.stats.isPlatform()) {
            Bitmap platform = BitmapFactory.decodeResource(
                    getResources(), PlatformService.getInstance().getPlatformImagePath(stat.platform));
            vSquareImage3.setImageBitmap(ImageHelper.getRoundedCornerBitmap(platform, 30));
            vTitle3.setText(stat.platform);
        }
    }

    public void setStat4(View view, StatisticsModels.StatisticsRow stat){
        TextView vTitle4 = view.findViewById(R.id.statistics_secondary_card_4_title);
        vTitle4.setText(stat.title);

        TextView vPlays4 = view.findViewById(R.id.statistics_secondary_card_4_plays);
        if(this.stats.isTop()){
            vPlays4.setText(String.valueOf(stat.total_plays));
        }
        if(this.stats.isPopular()){
            vPlays4.setText(String.valueOf(stat.users_watched));
        }

        NetworkImageView vImage4 = view.findViewById(R.id.statistics_secondary_card_4_image);
        NetworkImageView vCircleImage4 = view.findViewById(R.id.statistics_secondary_card_4_circle_image);
        ImageView vSquareImage4 = view.findViewById(R.id.statistics_secondary_card_4_square_image);

        if(this.stats.isTV()){
            vImage4.setImageUrl(UrlHelpers.getImageUrl(stat.grandparent_thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isMovie()) {
            vImage4.setImageUrl(UrlHelpers.getImageUrl(stat.thumb, "400", "600"),
                    ImageCacheManager.getInstance().getImageLoader());
        }

        if(this.stats.isUser()) {
            vCircleImage4.setImageUrl(stat.user_thumb, ImageCacheManager.getInstance().getImageLoader());
            vTitle4.setText(stat.friendly_name);
        }

        if(this.stats.isPlatform()) {
            Bitmap platform = BitmapFactory.decodeResource(
                    getResources(), PlatformService.getInstance().getPlatformImagePath(stat.platform));
            vSquareImage4.setImageBitmap(ImageHelper.getRoundedCornerBitmap(platform, 30));
            vTitle4.setText(stat.platform);
        }
    }

    public void setStats(StatisticsModels.StatisticsGroup stats) {
        this.stats = stats;
    }

    public void setType(String type) {
        this.type = type;
    }
}
