package com.williamcomartin.plexpyremote.UserActivities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.williamcomartin.plexpyremote.NavBaseActivity;
import com.williamcomartin.plexpyremote.R;

public class UserDetailActivity extends NavBaseActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_user_detail);
        setupActionBar();
        setupPager();

//        SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        intent = getIntent();
//        setupWatchStats();
//        setupPlayerStats();
    }

    @Override
    protected void setupDrawer(){
        super.setupDrawer();
        pageHasDrawer = false;
        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }
//
//    private void setupWatchStats() {
//        if (findViewById(R.id.watch_stats) != null) {
//
//            try {
//                String url = UrlHelpers.getHostPlusAPIKey()
//                        + "&cmd=get_user_watch_time_stats&user_id=" + ((Integer) intent.getIntExtra("ID", 0)).toString();
//
//                GsonRequest<UserWatchStatsModels> request = new GsonRequest<>(
//                        url,
//                        UserWatchStatsModels.class,
//                        null,
//                        new Response.Listener<UserWatchStatsModels>() {
//                            @Override
//                            public void onResponse(UserWatchStatsModels response) {
//                                for (UserWatchStatsModels.WatchStat stat : response.response.data){
//                                    UserWatchStatsFragment fragment = new UserWatchStatsFragment();
//                                    fragment.setStat(stat);
//                                    getSupportFragmentManager().beginTransaction().add(R.id.watch_stats, fragment).commit();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//                        }
//                );
//
//                RequestManager.addToRequestQueue(request);
//            } catch (NoServerException | MalformedURLException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }
//
//    private void setupPlayerStats() {
//        if (findViewById(R.id.player_stats) != null) {
//
//            try {
//                String url = UrlHelpers.getHostPlusAPIKey()
//                        + "&cmd=get_user_player_stats&user_id=" + ((Integer) intent.getIntExtra("ID", 0)).toString();
//
//                GsonRequest<UserPlayerStatsModels> request = new GsonRequest<>(
//                        url,
//                        UserPlayerStatsModels.class,
//                        null,
//                        new Response.Listener<UserPlayerStatsModels>() {
//                            @Override
//                            public void onResponse(UserPlayerStatsModels response) {
//                                for (UserPlayerStatsModels.PlayerStat stat : response.response.data){
//                                    UserPlayerStatsFragment fragment = new UserPlayerStatsFragment();
//                                    fragment.setStat(stat);
//                                    getSupportFragmentManager().beginTransaction().add(R.id.player_stats, fragment).commit();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//                        }
//                );
//
//                RequestManager.addToRequestQueue(request);
//            } catch (NoServerException | MalformedURLException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }

    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && extras != null) {
            actionBar.setTitle(extras.getString("UserName"));
        }
    }

    protected void setupPager() {
        ViewPager viewPager = findViewById(R.id.user_pager);
        UserPagerAdapter pagerAdapter = new UserPagerAdapter(getSupportFragmentManager(), extras.getString("UserID"));
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.user_taber);
        tabLayout.setupWithViewPager(viewPager);
    }
}
